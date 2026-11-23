import ComposeApp
import SwiftUI
import YouTubePlayerKit

public struct YouTubePlayerContainerView: View {
    public let videoID: String

    public var body: some View {
        YouTubePlayerView(
            YouTubePlayer(
                source: .video(id: videoID),
                parameters: .init(
                    autoPlay: true,
                    loopEnabled: true,
                    showControls: true,
                    showFullscreenButton: true
                )
            )
        )
        .frame(height: 250)
    }
}

