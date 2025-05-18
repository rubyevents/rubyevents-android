package org.rubyevents.app

object Router {
    private var baseURL: String

    init {
        this.baseURL = loadBaseURL()
    }

    private fun loadBaseURL(): String {
        if (BuildConfig.DEBUG) {
            return LOCAL_URL
        }
        return PRODUCTION_URL
    }

    val startURL: String
        get() { return baseURL }

    val pathConfigurationURL: String
        get() {return "$baseURL/hotwire/native/v1/android/path_configuration.json" }

    val eventsURL: String
        get() { return "$baseURL/events" }

    val talksURL: String
        get() { return "$baseURL/talks" }

    val speakersURL: String
        get() { return "$baseURL/speakers" }
}
