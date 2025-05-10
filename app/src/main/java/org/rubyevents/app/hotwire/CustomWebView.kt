package org.rubyevents.app.hotwire

import android.content.Context
import android.util.AttributeSet
import dev.hotwire.core.turbo.webview.HotwireWebView

class CustomWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : HotwireWebView(context, attrs) {
    init {
        // Disable media playback requiring user gesture
        // Without this, the videos on the web app won't play
        settings.mediaPlaybackRequiresUserGesture = false
    }
}