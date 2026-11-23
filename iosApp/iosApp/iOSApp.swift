import SwiftUI
import ComposeApp
import Firebase
import GoogleMaps
import mergn_ios
import Foundation
import UIKit
import WebKit

@main
struct iOSApp: App {

    init() {
        FirebaseApp.configure()
        MainViewControllerKt.initialize()

        // Initialize WebChat widget
        WebChatManager.shared.initializeWebChat()
        
        // Initialize MERGN independently of tracking permission
        TrackingManager.shared.initializeMergn()
    }
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    var body: some Scene {
        WindowGroup {
            SplashScreen()
                .onOpenURL { url in
                    // Add delay for killed state launches to ensure app is fully loaded
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                        NotificationCenter.default.post(name: .deepLinkReceived, object: url.absoluteString)
                    }
                }
        }
    }
}




