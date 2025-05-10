package org.rubyevents.app.hotwire.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.rubyevents.app.hotwire.CustomChromeClient
import dev.hotwire.core.turbo.webview.HotwireWebChromeClient
import dev.hotwire.navigation.destinations.HotwireDestinationDeepLink
import dev.hotwire.navigation.fragments.HotwireWebFragment
import org.rubyevents.app.R

@HotwireDestinationDeepLink(uri = "hotwire://fragment/web")
open class WebFragment : HotwireWebFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun createWebChromeClient(): HotwireWebChromeClient {
        return CustomChromeClient(navigator.session, activity)
    }

    override fun createProgressView(location: String): View {
        val progressView = R.layout.progress_view
        val progressbar = R.id.progress_bar

        // Show progress bar after 200ms
        Handler(Looper.getMainLooper()).postDelayed(
            {view?.findViewById<View>(progressbar)?.visibility = View.VISIBLE },
            200
        )

        return layoutInflater.inflate(progressView, null)
    }
}