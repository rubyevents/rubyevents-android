package org.rubyevents.app

object RubyEvents {
    val current: Environment = Environment.Remote

    enum class Environment(val url: String) {
        Remote("https://www.rubyevents.org"),
        Local("http://192.168.1.245:3000")
    }
}
