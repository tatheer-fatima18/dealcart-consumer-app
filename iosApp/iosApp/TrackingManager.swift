import AppTrackingTransparency
import UIKit
import AdSupport
import mergn_ios

class TrackingManager {
    static let shared = TrackingManager()
    
    private init() {}
    
    // Check current tracking status
    func getTrackingStatus() -> ATTrackingManager.AuthorizationStatus {
        return ATTrackingManager.trackingAuthorizationStatus
    }
    
    // Request tracking permission
    func requestTrackingPermission(completion: @escaping (ATTrackingManager.AuthorizationStatus) -> Void) {
        // Check iOS version - ATT is only available on iOS 14.5+
        if #available(iOS 14.5, *) {
            // Check if we can request permission
            guard ATTrackingManager.trackingAuthorizationStatus == .notDetermined else {
                completion(ATTrackingManager.trackingAuthorizationStatus)
                return
            }
            
            // Check if app is in active state - required for ATT dialog to appear
            guard UIApplication.shared.applicationState == .active else {
                completion(.notDetermined)
                return
            }
            
            // Request permission
            ATTrackingManager.requestTrackingAuthorization { status in
                DispatchQueue.main.async {
                    completion(status)
                }
            }
        } else {
            // On older iOS versions, assume tracking is allowed
            completion(.authorized)
        }
    }
    

    // Handle tracking permission result
    func handleTrackingPermission(_ status: ATTrackingManager.AuthorizationStatus) {
        switch status {
        case .authorized:
            // User allowed tracking
            UserDefaults.standard.set(true, forKey: "TRACKING_ENABLED")
            
        case .denied, .restricted:
            UserDefaults.standard.set(false, forKey: "TRACKING_ENABLED")
          
        case .notDetermined:
            // Haven't asked yet
            UserDefaults.standard.set(false, forKey: "TRACKING_ENABLED")
            
        @unknown default:
            UserDefaults.standard.set(false, forKey: "TRACKING_ENABLED")

        }
    }
    
    // Initialize MERGN independently of tracking permission
    func initializeMergn() {
        enableMergnTracking()
    }
    
    // Enable MERGN tracking
    private func enableMergnTracking() {
        // Read values from Info.plist
        let mergnApiKey = Bundle.main.object(forInfoDictionaryKey: "MERGN_API_KEY") as? String ?? ""
        let buildEnvironment = Bundle.main.object(forInfoDictionaryKey: "BUILD_ENVIRONMENT") as? String ?? ""
        let kotlinFrameworkBuildType = Bundle.main.object(forInfoDictionaryKey: "KOTLIN_FRAMEWORK_BUILD_TYPE") as? String ?? ""

        // Only register Mergn API if conditions are met
        if buildEnvironment == "prod" && kotlinFrameworkBuildType == "Release" && !mergnApiKey.isEmpty {
            let eventManager = EventManager.shared
            eventManager.registerAPI(clientApiKey: mergnApiKey)
            
            // Update FCM token if available
            if let fcmToken = getFCMToken() {
                eventManager.firebaseToken(token: fcmToken)
            }
        }
    }
    
    // Get FCM token from UserDefaults or other storage
    private func getFCMToken() -> String? {
        // You can store FCM token in UserDefaults when it's received
        return UserDefaults.standard.string(forKey: "FCM_TOKEN")
    }
}
