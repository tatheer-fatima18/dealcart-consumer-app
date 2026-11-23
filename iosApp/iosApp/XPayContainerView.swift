import SwiftUI
import XPayPaymentKit
import ComposeApp

struct XPayPaymentContainerView: UIViewControllerRepresentable {
    let publicKey: String
    let hmacSecretKey: String
    let accountId: String
    let paymentType: String
    
    var onPaymentConfirm: ((String, String, String, @escaping (String) -> Void) -> Void)?

    func makeUIViewController(context: Context) -> UIViewController {
        let keysConfiguration = KeysConfiguration(
            accountId: accountId,
            publicKey: publicKey,
            hmacKey: hmacSecretKey
        )
        let controller = XPayController()
        
        let paymentForm = XPayPaymentForm(
            keysConfiguration: keysConfiguration,
            onReady: { isReady in

                // Call the Kotlin onReady callback through the global function
                if let onReadyCallback = PaymentControllerManager.shared.cardOnReadyCallback {
                    onReadyCallback(isReady)
                }
            },
            controller: controller
        ).preferredColorScheme(.light)
        
        PaymentControllerManager.shared.cardController = controller
                
        let hostingController = UIHostingController(rootView: paymentForm)
        return hostingController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // No updates needed
    }
}

struct XPayEasyPaisaContainerView: UIViewControllerRepresentable {
    let publicKey: String
    let hmacSecretKey: String
    let accountId: String
    let paymentType: String
    
    var onPaymentConfirm: ((String, String, String, @escaping (String) -> Void) -> Void)?

    func makeUIViewController(context: Context) -> UIViewController {
        let keysConfiguration = KeysConfiguration(
            accountId: accountId,
            publicKey: publicKey,
            hmacKey: hmacSecretKey
        )

        let controller = XPayController()
        
        let easyPaisaForm = XPayEasyPaisaPaymentForm(
            keysConfiguration: keysConfiguration,
            onReady: { isReady in

                // Call the Kotlin onReady callback through the global function
                if let onReadyCallback = PaymentControllerManager.shared.easyPaisaOnReadyCallback {
                    onReadyCallback(isReady)
                }
            },
            controller: controller
        ).preferredColorScheme(.light)
        
        PaymentControllerManager.shared.easyPaisaController = controller
                
        let hostingController = UIHostingController(rootView: easyPaisaForm)
        return hostingController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // No updates needed
    }
}

class PaymentControllerManager: NSObject {
    static let shared = PaymentControllerManager()
    
    var cardController: XPayController?
    var easyPaisaController: XPayController?
    
    // Callback properties for onReady
    var cardOnReadyCallback: ((Bool) -> Void)?
    var easyPaisaOnReadyCallback: ((Bool) -> Void)?
        
    private override init() {
        super.init()
    }
    
    func confirmCardPayment(
            customerName: String,
            clientSecret: String,
            encryptionKey: String,
            callback: @escaping (String) -> Void
    ) {
        
                guard let controller = cardController else {
                    callback("{\"error\": true, \"message\": \"Controller not initialized\", \"status\": \"Failed\"}")
                    return
                }
        
        DispatchQueue.main.async {
                    controller.confirmPayment(
                        customerName: customerName,
                        clientSecret: clientSecret,
                        paymentResponse: { data in
                            let errorValue = data["error"] as? Bool ?? false
                            let message = data["message"] as? String ?? "payment error"
                            let paymentStatus = data["status"] as? String ?? "Failed"
                            
                            let response = """
                            {
                                "error": \(errorValue),
                                "message": "\(message)",
                                "status": "\(paymentStatus)"
                            }
                            """
                            callback(response)
                        }
                    )
                }
    }
    
    func confirmEasyPaisaPayment(
            customerName: String,
            clientSecret: String,
            encryptionKey: String,
            callback: @escaping (String) -> Void
    ) {
                guard let controller = easyPaisaController else {

                    callback("{\"error\": true, \"message\": \"Controller not initialized\", \"status\": \"Failed\"}")
                    return
                }
        
        DispatchQueue.main.async {
                    controller.confirmPayment(
                        customerName: customerName,
                        clientSecret: clientSecret,
                        paymentResponse: { data in
                            let errorValue = data["error"] as? Bool ?? false
                            let message = data["message"] as? String ?? "payment error"
                            let paymentStatus = data["status"] as? String ?? "Failed"
                            
                            let response = """
                            {
                                "error": \(errorValue),
                                "message": "\(message)",
                                "status": "\(paymentStatus)"
                            }
                            """
                            callback(response)
                        }
                    )
                }
    }
    
    func clearCard(){
        guard let controller = cardController else {
            return
        }
        
        DispatchQueue.main.async {
            controller.clear()
        }
    }
    
    func clearEasyPaisa(){
        guard let controller = easyPaisaController else {
            return
        }
        
        DispatchQueue.main.async {
            controller.clear()
        }
    }
    
    // Methods to set onReady callbacks
    func setCardOnReadyCallback(_ callback: @escaping (Bool) -> Void) {
        cardOnReadyCallback = callback
    }
    
    func setEasyPaisaOnReadyCallback(_ callback: @escaping (Bool) -> Void) {
        easyPaisaOnReadyCallback = callback
    }
}
