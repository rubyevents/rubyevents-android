package org.rubyevents.app.hotwire

import android.content.pm.ActivityInfo
import android.os.Build
import android.view.ViewGroup
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import dev.hotwire.core.turbo.session.Session
import dev.hotwire.core.turbo.webview.HotwireWebChromeClient
import java.lang.ref.WeakReference

class CustomChromeClient(session: Session, activity: FragmentActivity?) : HotwireWebChromeClient(session) {
    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var originalSystemUiVisibility = 0

    private val activityReference: WeakReference<FragmentActivity> = WeakReference(activity)

    // Override onShowCustomView to handle fullscreen video playback
    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        val activity = activityReference.get() ?: run {
            callback?.onCustomViewHidden()
            return
        }

        if (view == null || customView != null) {
            callback?.onCustomViewHidden()
            return
        }

        customView = view
        customViewCallback = callback
        showCustomViewInActivity(view, activity)
    }

    // Override onHideCustomView to handle exiting the fullscreen video playback
    override fun onHideCustomView() {
        val activity = activityReference.get() ?: return

        hideCustomViewInActivity(activity)
        customViewCallback?.onCustomViewHidden()
        customViewCallback = null
        resetOrientationAndVisibility(activity)
    }

    private fun showCustomViewInActivity(view: View, activity: FragmentActivity) {
        val decorView = activity.window.decorView as FrameLayout
        originalSystemUiVisibility = decorView.systemUiVisibility

        decorView.addView(view, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.apply {
                hide(android.view.WindowInsets.Type.systemBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    private fun hideCustomViewInActivity(activity: FragmentActivity) {
        val decorView = activity.window.decorView as FrameLayout
        decorView.removeView(customView)
        customView = null
    }

    private fun resetOrientationAndVisibility(activity: FragmentActivity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.show(WindowInsets.Type.systemBars())
        } else {
            val decorView = activity.window.decorView as FrameLayout
            decorView.systemUiVisibility = originalSystemUiVisibility
        }
    }
}