package org.rubyevents.app.hotwire.fragments

import android.os.Bundle
import dev.hotwire.navigation.destinations.HotwireDestinationDeepLink
import dev.hotwire.navigation.fragments.HotwireFragment
import org.rubyevents.app.MainActivity

@HotwireDestinationDeepLink(uri = "hotwire://fragment/refresh_app")
class RefreshAppFragment : HotwireFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator.reset()
        (activity as? MainActivity)?.delegate?.resetNavigators()
    }
}
