package org.rubyevents.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.navigation.activities.HotwireActivity
import dev.hotwire.navigation.tabs.HotwireBottomNavigationController
import dev.hotwire.navigation.util.applyDefaultImeWindowInsets
import dev.hotwire.navigation.tabs.navigatorConfigurations
import org.rubyevents.app.hotwire.bridge.OAuthComponent
import org.rubyevents.app.hotwire.tabs
import org.rubyevents.app.hotwire.viewmodels.MainActivityViewModel

class MainActivity : HotwireActivity() {
  private lateinit var bottomNavigationController: HotwireBottomNavigationController
  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    findViewById<View>(R.id.root).applyDefaultImeWindowInsets()
    initializeBottomTabs()
    handleOAuthRedirect(intent)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleOAuthRedirect(intent)
  }

  private fun handleOAuthRedirect(intent: Intent?) {
    if (intent?.action != Intent.ACTION_VIEW) return
    val uri = intent.data ?: return
    if (uri.scheme != "rubyevents" || uri.host != "auth") return
    OAuthComponent.handleRedirect(uri)
  }

  private fun initializeBottomTabs() {
    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

    bottomNavigationController = HotwireBottomNavigationController(this, bottomNavigationView)
    bottomNavigationController.load(tabs, viewModel.selectedTabIndex)
    bottomNavigationController.setOnTabSelectedListener { index, _ ->
      viewModel.selectedTabIndex = index
    }
  }

  override fun navigatorConfigurations() = tabs.navigatorConfigurations
}