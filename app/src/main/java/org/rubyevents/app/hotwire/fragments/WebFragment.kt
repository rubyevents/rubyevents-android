package org.rubyevents.app.hotwire.fragments

import org.rubyevents.app.hotwire.CustomChromeClient
import dev.hotwire.core.turbo.webview.HotwireWebChromeClient
import dev.hotwire.navigation.destinations.HotwireDestinationDeepLink
import dev.hotwire.navigation.fragments.HotwireWebFragment

@HotwireDestinationDeepLink(uri = "hotwire://fragment/web")
open class WebFragment : HotwireWebFragment() {
    override fun createWebChromeClient(): HotwireWebChromeClient {
        return CustomChromeClient(navigator.session, activity)
    }
}