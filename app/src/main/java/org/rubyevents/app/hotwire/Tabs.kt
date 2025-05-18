package org.rubyevents.app.hotwire

import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.tabs.HotwireBottomTab
import org.rubyevents.app.R
import org.rubyevents.app.Router

private val home = HotwireBottomTab(
    title = "Home",
    iconResId = R.drawable.ic_tab_home,
    configuration = NavigatorConfiguration(
        name = "home",
        navigatorHostId = R.id.home_navigator_host,
        startLocation = Router.startURL
    )
)

private val events = HotwireBottomTab(
    title = "Events",
    iconResId = R.drawable.ic_tab_events,
    configuration = NavigatorConfiguration(
        name = "events",
        navigatorHostId = R.id.events_navigator_host,
        startLocation = Router.eventsURL
    )
)

private val talks = HotwireBottomTab(
    title = "Talks",
    iconResId = R.drawable.ic_tab_talks,
    configuration = NavigatorConfiguration(
        name = "talks",
        navigatorHostId = R.id.talks_navigator_host,
        startLocation = Router.talksURL
    )
)

private val speakers = HotwireBottomTab(
    title = "Speakers",
    iconResId = R.drawable.ic_tab_speakers,
    configuration = NavigatorConfiguration(
        name = "speakers",
        navigatorHostId = R.id.speakers_navigator_host,
        startLocation = Router.speakersURL
    )
)

val tabs = listOf(
    home,
    events,
    talks,
    speakers
)
