import ComposeApp
import FirebaseCore
import FirebaseMessaging
import UIKit
import UserNotifications
import GoogleMaps
import Foundation
import mergn_ios
import AppTrackingTransparency
import CoreLocation
import Singular

// MARK: - Logging Utility
class NotificationLogger {
    static let shared = NotificationLogger()
    private let logFileName = "notification_logs.txt"
    
    private func getLogFilePath() -> String {
        let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0]
        return (documentsPath as NSString).appendingPathComponent(logFileName)
    }
    
    func log(_ message: String) {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
        let timestamp = formatter.string(from: Date())
        
        let logMessage = "[\(timestamp)] \(message)\n"
        
        // Also print to console for debugging
        print(logMessage.trimmingCharacters(in: .whitespacesAndNewlines))
        
        // Write to file
        if let data = logMessage.data(using: .utf8) {
            let filePath = getLogFilePath()
            if let fileHandle = FileHandle(forWritingAtPath: filePath) {
                fileHandle.seekToEndOfFile()
                fileHandle.write(data)
                fileHandle.closeFile()
            } else {
                // Create file if it doesn't exist
                try? data.write(to: URL(fileURLWithPath: filePath))
            }
        }
    }
    
    func clearLogs() {
        let filePath = getLogFilePath()
        try? FileManager.default.removeItem(atPath: filePath)
        log("Logs cleared")
    }
    
    func getLogs() -> String {
        let filePath = getLogFilePath()
        return (try? String(contentsOfFile: filePath, encoding: .utf8)) ?? "No logs found"
    }
}

class AppDelegate: UIResponder, UIApplicationDelegate, InAppMergnDelegate {

    // MARK: - Static FCM Token Access
    static var fcmToken: String = ""

    // MARK: - Notification Deduplication
    
    private var processedNotificationIds: Set<String> = []
    private let notificationIdQueue = DispatchQueue(label: "notification.deduplication")

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication
            .LaunchOptionsKey: Any]?
    ) -> Bool {
        
        // Configure window appearance for white background with grey home indicator
        configureWindowAppearance()
        
        // Set UNUserNotificationCenter delegate immediately at launch
        UNUserNotificationCenter.current().delegate = self
        
        // Set messaging delegate after Firebase is configured
        Messaging.messaging().delegate = self

        // Handle notification if app was launched from notification
        if let notification = launchOptions?[.remoteNotification] as? [AnyHashable: Any] {
            DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                self.handleNotificationTap(userInfo: notification)
            }
        }
        
        // Handle deep link if app was launched from deep link
        if let url = launchOptions?[.url] as? URL {
            // Add delay for killed state launches to ensure app is fully loaded
            DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
                NotificationCenter.default.post(name: .deepLinkReceived, object: url.absoluteString)
            }
        }
        
        // Clear old notifications to prevent spam
        clearOldNotifications()
        
        // Request tracking permission after a longer delay to ensure app is fully loaded
        DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
            self.requestTrackingPermission()
        }
        
        // Initialize Singular SDK
        if let config = getSingularConfig() {
            Singular.start(config)
        } else {
            print("🔴 [Singular] Failed to initialize Singular SDK - config creation failed")
        }

        // Assign MERGN in-app deep link delegate to client app
        MergnSDKManager.shared.inAppDelegateMergn = self

        return true
    }

    // MARK: - Window Appearance Configuration
    
    private func configureWindowAppearance() {
        // Configure window appearance for white background with grey home indicator
        DispatchQueue.main.async {
            if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
                windowScene.windows.forEach { window in
                    // Set window background to white
                    window.backgroundColor = .white
                    
                    // Configure home indicator appearance (iOS 13+)
                    // Set to light mode so home indicator appears dark/grey on white background
                    if #available(iOS 13.0, *) {
                        window.overrideUserInterfaceStyle = .light
                    }
                    
                    // Configure root view controller to extend under status bar
                    if let rootViewController = window.rootViewController {
                        // Remove safe area insets for status bar (edge-to-edge)
                        rootViewController.additionalSafeAreaInsets = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
                        
                        // Ensure view extends to full screen bounds (iOS 11+)
                        if #available(iOS 11.0, *) {
                            rootViewController.view.frame = UIScreen.main.bounds
                        }
                    }
                    
                    // Configure navigation bar appearance
                    let navBarAppearance = UINavigationBarAppearance()
                    navBarAppearance.configureWithOpaqueBackground()
                    navBarAppearance.backgroundColor = .white
                    UINavigationBar.appearance().standardAppearance = navBarAppearance
                    UINavigationBar.appearance().scrollEdgeAppearance = navBarAppearance
                    
                    // Configure tab bar appearance
                    let tabBarAppearance = UITabBarAppearance()
                    tabBarAppearance.configureWithOpaqueBackground()
                    tabBarAppearance.backgroundColor = .white
                    UITabBar.appearance().standardAppearance = tabBarAppearance
                    if #available(iOS 15.0, *) {
                        UITabBar.appearance().scrollEdgeAppearance = tabBarAppearance
                    }
                }
            }
        }
    }
    
    // MARK: - InAppMergnDelegate
    func DidReceiveInAppDeepLink(_ url: String) {
        // Forward MERGN in-app deep links to existing deep link flow
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            NotificationCenter.default.post(name: .deepLinkReceived, object: url)
        }
    }

    // MARK: - Singular Configuration
    
    private func getSingularConfig() -> SingularConfig? {
        let apiKey = Bundle.main.object(forInfoDictionaryKey: "SINGULAR_API_KEY") as? String ?? ""
        let secret = Bundle.main.object(forInfoDictionaryKey: "SINGULAR_SECRET") as? String ?? ""
        guard !apiKey.isEmpty, !secret.isEmpty,
              let config = SingularConfig(apiKey: apiKey, andSecret: secret) else {
            return nil
        }
        
        
        // Set 300 sec delay to wait for ATT response
        config.waitForTrackingAuthorizationWithTimeoutInterval = 300
        
        // Support custom ESP domains
        config.espDomains = ["links.dealcart.io", "links-dev.dealcart.io", "links-staging.dealcart.io"]
    
        // Set deep link handler
        config.singularLinksHandler = { params in
            self.handleSingularDeepLink(params: params)
        }
        
        return config
    }
    
    private func handleSingularDeepLink(params: SingularLinkParams?) {
        // Handle Singular deep link parameters
        // You can integrate this with your existing deep link handling
        guard let params = params else {
            return 
        }
        
       
        // Extract deep link URL from SingularLinkParams
        if let deepLink = params.getDeepLink() {
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                NotificationCenter.default.post(name: .deepLinkReceived, object: deepLink)
            }
        }
    }
    

    
    // MARK: - Tracking Permission
    
    private func requestTrackingPermission() {
        TrackingManager.shared.requestTrackingPermission { status in
            TrackingManager.shared.handleTrackingPermission(status)
            
            // Request notification permissions after tracking permission is handled
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                self.requestNotificationPermissions(application: UIApplication.shared)
            }
        }
    }
    
    // MARK: - Notification Permissions

    private func requestNotificationPermissions(application: UIApplication) {
        UNUserNotificationCenter.current().delegate = self

        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: { granted, error in
                DispatchQueue.main.async {
                    if granted {
                        application.registerForRemoteNotifications()
                    }
                    
                    // Request location permission after notification permission is completed
                    self.requestLocationPermission()
                }
            }
        )
    }
    
    private func requestLocationPermission() {
        let locationManager = CLLocationManager()
        locationManager.delegate = self
        
        // Request precise location permission
        if #available(iOS 14.0, *) {
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.requestWhenInUseAuthorization()
        } else {
            locationManager.requestWhenInUseAuthorization()
        }
    }

    // MARK: - Remote Notifications

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        // Set the APNS token for Firebase
        Messaging.messaging().apnsToken = deviceToken

        // Add a small delay to ensure APNS token is properly set before getting FCM token
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            Messaging.messaging().token { token, error in
                if let token = token {
                    self.updateTokenInPreferences(token: token)
                }
            }
        }
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        // Handle registration failure silently
    }

    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable: Any],
        fetchCompletionHandler completionHandler: @escaping (
            UIBackgroundFetchResult
        ) -> Void
    ) {

        // Don't handle navigation for background notifications - only when user taps
        // The system will show the notification and user can tap to navigate
        handleNotificationReceive(userInfo: userInfo)
        completionHandler(.newData)
    }
    
    // Handle silent notifications (data-only payloads) when app is terminated
    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable: Any]
    ) {
        // Local notification creation disabled - only use APNS
        handleNotificationReceive(userInfo: userInfo)
    }
    
    // Handle background app refresh
    func application(
        _ application: UIApplication,
        performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void
    ) {
        completionHandler(.newData)
    }
    
    // MARK: - Deep Link Handling
    
    func application(_ application: UIApplication,
                     continue userActivity: NSUserActivity,
                     restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        if userActivity.activityType == NSUserActivityTypeBrowsingWeb,
           let url = userActivity.webpageURL {
            // Add delay for killed state launches
            DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
                NotificationCenter.default.post(name: .deepLinkReceived, object: url.absoluteString)
            }
            return true
        }
        return false
    }

    func application(_ app: UIApplication, open url: URL,
                     options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        // Add delay for killed state launches
        DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
            NotificationCenter.default.post(name: .deepLinkReceived, object: url.absoluteString)
        }
        return true
    }
}

// MARK: - MessagingDelegate

extension AppDelegate: MessagingDelegate {

    func messaging(
        _ messaging: Messaging,
        didReceiveRegistrationToken fcmToken: String?
    ) {
        guard let token = fcmToken else { return }

        // Update token in shared preferences
        updateTokenInPreferences(token: token)
        
    
    }
    
    private func updateTokenInPreferences(token: String) {
        // Store token in UserDefaults for persistent storage
        UserDefaults.standard.set(token, forKey: "FCM_TOKEN")
    }
    

    
    private func shouldProcessNotification(_ userInfo: [AnyHashable: Any]) -> Bool {
        // Create a unique identifier for this notification
        let messageId = userInfo["gcm.message_id"] as? String ?? ""
        let title = extractNotificationTitle(from: userInfo)
        let body = extractNotificationBody(from: userInfo)
        let timestamp = userInfo["google.c.fid"] as? String ?? ""
        
        let notificationId = "\(messageId)-\(title)-\(body)-\(timestamp)"
        
        return notificationIdQueue.sync {
            if processedNotificationIds.contains(notificationId) {
                return false
            } else {
                processedNotificationIds.insert(notificationId)
                
                // Keep only last 100 notification IDs to prevent memory issues
                if processedNotificationIds.count > 100 {
                    processedNotificationIds.removeAll()
                }
                
                return true
            }
        }
    }
    
    private func shouldCreateLocalNotification(_ userInfo: [AnyHashable: Any]) -> Bool {
        // Disabled local notification creation - only use APNS
        return false
    }
    
    private func extractNotificationTitle(from userInfo: [AnyHashable: Any]) -> String {
        // Try to get title from APNS payload structure first
        if let aps = userInfo["aps"] as? [String: Any],
           let alert = aps["alert"] as? [String: Any],
           let title = alert["title"] as? String, !title.isEmpty {
            return title
        }
        
        // Fallback to data section if APNS not available
        if let title = userInfo["title"] as? String, !title.isEmpty {
            return title
        }
        
        return ""
    }
    
    private func extractNotificationBody(from userInfo: [AnyHashable: Any]) -> String {
        // Try to get body from APNS payload structure first
        if let aps = userInfo["aps"] as? [String: Any],
           let alert = aps["alert"] as? [String: Any],
           let body = alert["body"] as? String, !body.isEmpty {
            return body
        }
        
        // Fallback to data section if APNS not available
        if let body = userInfo["body"] as? String, !body.isEmpty {
            return body
        }
        
        return ""
    }
    
    private func clearOldNotifications() {
        // Clear delivered notifications older than 1 hour to prevent spam
        UNUserNotificationCenter.current().getDeliveredNotifications { notifications in
            let oneHourAgo = Date().timeIntervalSince1970 - 3600
            let oldNotifications = notifications.filter { notification in
                // notification.date is not optional, so we can use it directly
                return notification.date.timeIntervalSince1970 < oneHourAgo
            }
            
            if !oldNotifications.isEmpty {
                let identifiers = oldNotifications.map { $0.request.identifier }
                UNUserNotificationCenter.current().removeDeliveredNotifications(withIdentifiers: identifiers)
            }
        }
    }
    

    

}

// MARK: - UNUserNotificationCenterDelegate

extension AppDelegate: UNUserNotificationCenterDelegate {

    // Handle notification when app is in foreground
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (
            UNNotificationPresentationOptions
        ) -> Void
    ) {
        // Mergn Call
        EventManager.shared.notificationViewed(notificationData: notification.request)

        let userInfo = notification.request.content.userInfo
        
        // Check if this is a refresh_cart notification
        if let redirection = userInfo["redirection"] as? String, redirection == "refresh_cart" {
            // Don't show notification, just refresh cart silently
            handleNotificationReceive(userInfo: userInfo)
            completionHandler([]) // Don't show notification
        } else {
            handleNotificationReceive(userInfo: userInfo)
            // Only update notification manager for foreground display, don't trigger navigation
            updateNotificationManagerForForeground(userInfo: userInfo)

            // Show notification even when app is in foreground - use all available options
            if #available(iOS 14.0, *) {
                completionHandler([[.banner, .badge, .sound, .list]])
            } else {
                completionHandler([[.alert, .badge, .sound]])
            }
        }
    }

    // Handle notification tap
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        // Mergn Call
        EventManager.shared.notificationTapped(notificationData: response.notification.request)

        let userInfo = response.notification.request.content.userInfo
        handleNotificationTap(userInfo: userInfo)

        completionHandler()
    }



    private func handleNotificationTap(userInfo: [AnyHashable: Any]) {
        // Check if notification has notificationAction (deep link)
        if let notificationAction = userInfo["notificationAction"] as? String, !notificationAction.isEmpty {
            // Handle as deep link - use longer delay for cold start
            DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
                NotificationCenter.default.post(name: .deepLinkReceived, object: notificationAction)
            }
            return
        }
        
        // Check if notification has url (alternative deep link key)
        if let url = userInfo["url"] as? String, !url.isEmpty {
            // Handle as deep link - use longer delay for cold start
            DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
                NotificationCenter.default.post(name: .deepLinkReceived, object: url)
            }
            return
        }
        
        // If no deep link, proceed with normal notification handling
        let data = notificationData(from: userInfo)

        // Send to Kotlin notification handler to trigger navigation
        DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
            NotificationUtils_iosKt.extractNotificationData(intent: data)
        }
    }

    private func handleNotificationReceive(userInfo: [AnyHashable: Any]) {
        let data = notificationData(from: userInfo)
        print("NOTIF_PASSIVE | AppDelegate handleNotificationReceive data=\(data)")
        NotificationUtils_iosKt.handlePassiveNotificationData(intent: data)
    }

    private func notificationData(from userInfo: [AnyHashable: Any]) -> [String: String] {
        var data: [String: String] = [:]

        if let nestedData = userInfo["data"] as? [AnyHashable: Any] {
            for (key, value) in nestedData {
                if let stringKey = key as? String, let stringValue = value as? String {
                    data[stringKey] = stringValue
                }
            }
        }

        for (key, value) in userInfo {
            if let stringKey = key as? String, let stringValue = value as? String {
                if data[stringKey] == nil {
                    data[stringKey] = stringValue
                }
            }
        }

        data["title"] = extractNotificationTitle(from: userInfo)
        data["body"] = extractNotificationBody(from: userInfo)
        if let imageUrl = userInfo["imageUrl"] as? String {
            data["imageUrl"] = imageUrl
        }

        return data
    }




    
    private func updateNotificationManagerForForeground(userInfo: [AnyHashable: Any]) {
        // Extract notification data for foreground display only
        let title = extractNotificationTitle(from: userInfo)
        let body = extractNotificationBody(from: userInfo)
        let imageUrl = userInfo["imageUrl"] as? String
        
        // Create notification data for display purposes only
        var notificationData: [String: String] = [:]
        for (key, value) in userInfo {
            if let stringKey = key as? String,
                let stringValue = value as? String
            {
                notificationData[stringKey] = stringValue
            }
        }
        
        // Add title and body to the data map
        notificationData["title"] = title
        notificationData["body"] = body
        if let imageUrl = imageUrl {
            notificationData["imageUrl"] = imageUrl
        }
        
        // Only update for display, don't trigger navigation
    }
    
    // MARK: - Logging Access Methods
    
    func getNotificationLogs() -> String {
        return NotificationLogger.shared.getLogs()
    }
    
    func clearNotificationLogs() {
        NotificationLogger.shared.clearLogs()
    }
}

// MARK: - CLLocationManagerDelegate

extension AppDelegate: CLLocationManagerDelegate {
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
        case .authorizedWhenInUse, .authorizedAlways:
            // Location permission granted, you can now use location services
            manager.startUpdatingLocation()
        
        @unknown default:
            fatalError()
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }
        
        // Stop updating location after getting the first location
        manager.stopUpdatingLocation()
        
        // You can store this location or use it as needed
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        manager.stopUpdatingLocation()
    }
}

// MARK: - Deep Link Notification Extension

extension Notification.Name {
    static let deepLinkReceived = Notification.Name("deepLinkReceived")
}


