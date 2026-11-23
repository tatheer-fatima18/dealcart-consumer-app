import ComposeApp
import SwiftUI
import mergn_ios
import Singular

struct ComposeView: UIViewControllerRepresentable {
    var initialLat: KotlinDouble?
    var initialLng: KotlinDouble?
    var initialZoom: KotlinDouble?

    func makeUIViewController(context: Context) -> UIViewController {
        let controller = MainViewControllerKt.MainViewController(
            mapUIViewController: { onLocationSelected, initialLat, initialLng, initialZoom in
                let swiftCallback: ((KotlinDouble, KotlinDouble) -> KotlinUnit)? = onLocationSelected.map { callback in
                    { lat, lng in
                        callback(lat, lng)
                        return KotlinUnit()
                    }
                }
                
                let controller = UIHostingController(
                    rootView: MapView(
                        onLocationSelected: swiftCallback,
                        initialLat: initialLat,
                        initialLng: initialLng,
                        initialZoom: initialZoom
                    )
                )
                
                // Wire up the camera updater function
                MainViewControllerKt.setMapCamera = { lat, lng, zoom in
                    DispatchQueue.main.async {
                        controller.rootView = MapView(
                            onLocationSelected: swiftCallback,
                            initialLat: lat,
                            initialLng: lng,
                            initialZoom: zoom
                        )
                    }
                }
                
                return controller
            },
            youTubePlayerUIViewController: { videoId in
                let swiftVideoId = videoId ?? ""
                return UIHostingController(
                    rootView: YouTubePlayerContainerView(videoID: swiftVideoId)
                )
            },
            xpayPaymentElementUIViewController: { publicKey, hmacSecretKey, accountId, paymentType in
                return UIHostingController(
                    rootView: XPayPaymentContainerView(
                        publicKey: publicKey,
                        hmacSecretKey: hmacSecretKey,
                        accountId: accountId,
                        paymentType: paymentType
                    )
                )
            },
            xpayEasyPaisaElementUIViewController: { publicKey, hmacSecretKey, accountId, paymentType in
                return UIHostingController(
                    rootView: XPayEasyPaisaContainerView(
                        publicKey: publicKey,
                        hmacSecretKey: hmacSecretKey,
                        accountId: accountId,
                        paymentType: paymentType
                    )
                )
            },
            // Add these new payment confirmation functions
            confirmCardPaymentFunction: { customerName, clientSecret, encryptionKey, callback in
                PaymentControllerManager.shared.confirmCardPayment(
                    customerName: customerName,
                    clientSecret: clientSecret,
                    encryptionKey: encryptionKey
                ) { response in
                    callback(response)
                }
            },
            confirmEasyPaisaPaymentFunction: { customerName, clientSecret, encryptionKey, callback in
                PaymentControllerManager.shared.confirmEasyPaisaPayment(
                    customerName: customerName,
                    clientSecret: clientSecret,
                    encryptionKey: encryptionKey
                ) { response in
                    callback(response)
                }
            },
            clearCardFunction: {
                PaymentControllerManager.shared.clearCard()
            },
            clearEasyPaisaFunction: {
                PaymentControllerManager.shared.clearEasyPaisa()
            },
            onEvent: { eventName, properties in
                do {
                    let eventPropertiesMap = properties as [String: Any]
                    EventManager.shared.sendEvent(eventName: eventName, properties: eventPropertiesMap)
                } catch {}
            },
            onAttribute: { attributeName, attributeValue in
                do {
                    EventManager.shared.sendAttribute(attributeName: attributeName, attributeValue: attributeValue)
                } catch {}
            },
            onIdentification: { identity in
                do {
                    EventManager.shared.postIdentification(identity: identity)
                    Singular.setCustomUserId("custom_user_id")
                } catch {}
            },
            onSingularCustomUserId: { userId in
                do {
                    Singular.setCustomUserId(userId)
                } catch {}
            },
            onSingularUnsetCustomUserId: {
                do {
                    Singular.unsetCustomUserId()
                } catch {}
            },
            onSingularEvent: { eventName, properties in
                do {
                    var singularProperties: [AnyHashable: Any] = [:]
                    for (key, value) in properties {
                        singularProperties[key] = value
                    }
                    Singular.event(eventName, withArgs: singularProperties)
                } catch {}
            },
            onSingularCustomRevenue: { eventName, currency, amount, properties in
                do {
                    var singularProperties: [AnyHashable: Any] = [:]
                    for (key, value) in properties {
                        singularProperties[key] = value
                    }
                    Singular.customRevenue(eventName, currency: currency, amount: Double(amount), withAttributes: singularProperties)
                } catch {}
            },

            // Add onReady callback functions
            cardOnReadyFunction: { callback in
                PaymentControllerManager.shared.setCardOnReadyCallback { isReady in
                    callback(KotlinBoolean(value: isReady))
                }
            },
            easyPaisaOnReadyFunction: { callback in
                PaymentControllerManager.shared.setEasyPaisaOnReadyCallback { isReady in
                    callback(KotlinBoolean(value: isReady))
                }
            }
        )
        
        // Configure view controller to extend under status bar (edge-to-edge)
        controller.edgesForExtendedLayout = .all
        controller.extendedLayoutIncludesOpaqueBars = true
        
        // Remove safe area insets for status bar (iOS 11+)
        if #available(iOS 11.0, *) {
            controller.additionalSafeAreaInsets = UIEdgeInsets.zero
            // Ensure view extends to full screen bounds
            controller.view.frame = UIScreen.main.bounds
        }
        
        return controller
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Ensure configuration persists
        uiViewController.edgesForExtendedLayout = .all
        uiViewController.extendedLayoutIncludesOpaqueBars = true
        if #available(iOS 11.0, *) {
            uiViewController.additionalSafeAreaInsets = UIEdgeInsets.zero
            // Ensure view extends to full screen bounds
            uiViewController.view.frame = UIScreen.main.bounds
        }
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.all, edges: .all) // Ignore all safe areas including status bar
            .onOpenURL { url in
                // Handle Singular deep links
                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                    NotificationCenter.default.post(name: .deepLinkReceived, object: url.absoluteString)
                }
            }
    }
}
