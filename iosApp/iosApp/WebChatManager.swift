import Foundation
import WebKit

class WebChatManager {
    static let shared = WebChatManager()
    
    private var webView: WKWebView?
    
    private init() {}
    
    func initializeWebChat() {
        // Create WebView configuration
        let config = WKWebViewConfiguration()
        config.preferences = WKPreferences()
        config.preferences.javaScriptCanOpenWindowsAutomatically = true
        config.allowsInlineMediaPlayback = true
        config.mediaTypesRequiringUserActionForPlayback = []
        config.websiteDataStore = WKWebsiteDataStore.default()
        config.allowsAirPlayForMediaPlayback = true
        config.allowsPictureInPictureMediaPlayback = true
        config.suppressesIncrementalRendering = false
        
        // Create WebView with zero frame (hidden)
        webView = WKWebView(frame: .zero, configuration: config)
        
        // Load WebChat HTML content
        let webChatHtml = """
        <!DOCTYPE html>
        <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <script type="text/javascript" 
                    src="https://dealcart.callcenterstudio.com/click2connects/click2connect.js?widget_key=ahRzfm11c3RlcmktaGl6bWV0bGVyaXIhCxIUQ2xpY2syQ29ubmVjdFBhY2thZ2UYgICKsdKajQoMogEdZGVhbGNhcnQuY2FsbGNlbnRlcnN0dWRpby5jb20">
                </script>
                <script type="text/javascript">
                    window.onload = function() {
                        try {
                             startWidget();
                             _Click2ConnectWidgetMinimise();                        
                        } catch(e) {
                            console.error('Error initializing widget:', e);
                        }
                    }
                </script>
            </head>
            <body style="margin:0; padding:0;">
            </body>
        </html>
        """
        
        // Load the HTML content
        webView?.loadHTMLString(webChatHtml, baseURL: URL(string: "https://dealcart.callcenterstudio.com"))
    }
}
