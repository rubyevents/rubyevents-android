package org.rubyevents.app

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.navigation.activities.HotwireActivity
import dev.hotwire.navigation.tabs.HotwireBottomNavigationController
import dev.hotwire.navigation.util.applyDefaultImeWindowInsets
import dev.hotwire.navigation.tabs.navigatorConfigurations
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