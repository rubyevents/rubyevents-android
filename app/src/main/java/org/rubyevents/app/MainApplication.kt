package org.rubyevents.app

import android.app.Application
import dev.hotwire.core.BuildConfig
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        configureApp()
    }

    private fun configureApp() {
        // PathConfiguration
        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                remoteFileUrl = "https://www.rubyevents.org/hotwire/native/v1/ios/path_configuration.json"
            )
        )

        // General configuration
        Hotwire.config.debugLoggingEnabled = BuildConfig.DEBUG
        Hotwire.config.webViewDebuggingEnabled = BuildConfig.DEBUG
    }
}
