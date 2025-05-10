package org.rubyevents.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dev.hotwire.core.BuildConfig
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration
import dev.hotwire.navigation.config.defaultFragmentDestination
import dev.hotwire.navigation.config.registerBridgeComponents
import dev.hotwire.navigation.config.registerFragmentDestinations
import dev.hotwire.navigation.fragments.HotwireWebBottomSheetFragment
import org.rubyevents.app.hotwire.bridge.ButtonComponent
import org.rubyevents.app.hotwire.fragments.WebFragment
import org.rubyevents.app.hotwire.CustomWebView

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        configureApp()
    }

    private fun configureApp() {
        // Disable dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Default fragment
        Hotwire.defaultFragmentDestination = WebFragment::class

        // All available fragments
        Hotwire.registerFragmentDestinations(
            WebFragment::class,
            HotwireWebBottomSheetFragment::class,
        )

        // PathConfiguration
        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                remoteFileUrl = "${RubyEvents.current.url}/hotwire/native/v1/android/path_configuration.json"
            )
        )

        // Bridge components
        Hotwire.registerBridgeComponents(
            BridgeComponentFactory("button", ::ButtonComponent)
        )

        // Custom WebView
        Hotwire.config.makeCustomWebView = { context ->
            CustomWebView(context, null)
        }

        // General configuration
        Hotwire.config.debugLoggingEnabled = BuildConfig.DEBUG
        Hotwire.config.webViewDebuggingEnabled = BuildConfig.DEBUG
        Hotwire.config.jsonConverter = KotlinXJsonConverter()
    }
}
