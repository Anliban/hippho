package com.anliban.team.hippho.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.ActivityMainBinding
import com.anliban.team.hippho.util.activityViewModel
import com.anliban.team.hippho.util.consume
import dagger.android.support.DaggerAppCompatActivity
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private companion object {
        private val TOP_LEVEL_DESTINATION = setOf(
            R.id.homeFragment,
            R.id.settingFragment,
            R.id.infoFragment
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by activityViewModel {
        viewModelFactory.create(
            MainViewModel::class.java
        )
    }

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        binding.apply {
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity
        }

        binding.navigationView.doOnApplyWindowInsets { navigationView, insets, initialState ->
            navigationView.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop
            )
        }

        binding.navigationView.setNavigationItemSelectedListener {
            consume {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                when (it.itemId) {
                    R.id.home -> navController.popBackStack(R.id.home, false)
                    R.id.setting -> navController.navigate(R.id.action_to_setting)
                    R.id.info -> navController.navigate(R.id.action_to_info)
                    else -> NavigationUI.onNavDestinationSelected(it, navController)
                }
            }
        }

        binding.toolbar.doOnLayout {
            invalidateOptionsMenu()
        }

        if (savedInstanceState == null) {
            binding.navigationView.setCheckedItem(R.id.home)
        }

        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATION, binding.drawerLayout)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            onDrawerLockMode(destination)
            onDestinationChanged(destination)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    private fun onDestinationChanged(destination: NavDestination) {
        when (destination.id) {
            R.id.homeFragment -> {
                supportActionBar?.title = "Hippo"
                binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_menu_black_24dp)
            }
            R.id.detailFragment -> {
                supportActionBar?.title = "Detail"
                binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_black_24dp)
            }
            R.id.infoFragment -> {
                binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_black_24dp)
                supportActionBar?.title = "Info"
            }
            R.id.settingFragment -> {
                binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_black_24dp)
                supportActionBar?.title = "Setting"
            }
        }
    }

    private fun onDrawerLockMode(destination: NavDestination) {
        val isTopLevelDestination = TOP_LEVEL_DESTINATION.contains(destination.id)
        val lockMode = if (isTopLevelDestination) {
            DrawerLayout.LOCK_MODE_UNLOCKED
        } else {
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        }
        binding.drawerLayout.setDrawerLockMode(lockMode)
    }
}
