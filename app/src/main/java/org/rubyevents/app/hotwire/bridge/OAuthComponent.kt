package org.rubyevents.app.hotwire.bridge

import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import dev.hotwire.navigation.fragments.HotwireFragment
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.rubyevents.app.R
import org.rubyevents.app.Router

class OAuthComponent(
    name: String,
    private val bridgeDelegate: BridgeDelegate<HotwireDestination>
) : BridgeComponent<HotwireDestination>(name, bridgeDelegate) {

    private val fragment: HotwireFragment
        get() = bridgeDelegate.destination.fragment as HotwireFragment

    private var pendingSignIn: Message? = null

    override fun onReceive(message: Message) {
        Log.d(TAG, "onReceive event=${message.event} jsonData=${message.jsonData}")
        when (message.event) {
            "signIn" -> handleSignIn(message)
            "disconnect" -> { pendingSignIn = null }
            else -> Log.w(TAG, "Unknown event: ${message.event}")
        }
    }

    override fun onStart() {
        fragment.viewLifecycleOwner.lifecycleScope.launch {
            fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                callbacks.collect { onRedirect(it) }
            }
        }
    }

    private fun handleSignIn(message: Message) {
        val path = message.data<MessageData>()?.startPath ?: return
        val url = resolveAgainstBaseUrl(path)
        Log.d(TAG, "handleSignIn url=$url")
        pendingSignIn = message
        clearCallbacks()
        launchCustomTab(url)
    }

    private fun resolveAgainstBaseUrl(path: String): String {
        return Router.startURL.trimEnd('/') + (if (path.startsWith("/")) path else "/$path")
    }

    private fun launchCustomTab(url: String) {
        val context = fragment.requireContext()
        val brandColor = ContextCompat.getColor(context, R.color.main_app_color)
        CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(brandColor)
                    .build()
            )
            .setShowTitle(true)
            .build()
            .launchUrl(context, Uri.parse(url))
    }

    private fun onRedirect(uri: Uri) {
        val message = pendingSignIn ?: return
        pendingSignIn = null
        clearCallbacks()
        val token = uri.getQueryParameter("token")
        replyWith(message.replacing(event = "success", data = SuccessData(token = token)))
        Log.d(TAG, "Replied success for $uri (originalEvent=${message.event})")
    }

    @Serializable
    data class MessageData(val startPath: String? = null)

    @Serializable
    data class SuccessData(val token: String? = null)

    companion object {
        private const val TAG = "OAuthComponent"

        private val callbacksFlow = MutableSharedFlow<Uri>(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        private val callbacks: SharedFlow<Uri> = callbacksFlow

        fun handleRedirect(uri: Uri) {
            callbacksFlow.tryEmit(uri)
        }

        private fun clearCallbacks() {
            callbacksFlow.resetReplayCache()
        }
    }
}
