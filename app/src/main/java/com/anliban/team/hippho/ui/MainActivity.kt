package com.anliban.team.hippho.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.ActivityMainBinding
import com.anliban.team.hippho.util.consume
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private companion object {
        private val TOP_LEVEL_DESTINATION = setOf(
            R.id.homeFragment,
            R.id.settingFragment,
            R.id.infoFragment
        )
    }

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

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

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            binding.root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
            view.updatePadding(
                left = insets.systemWindowInsetLeft + initialState.paddings.left,
                right = insets.systemWindowInsetRight + initialState.paddings.right
            )
        }.applyToView(binding.contentContainer)

        Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
            view.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop
            )
        }.applyToView(binding.navigationView)

        Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
            view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = insets.systemWindowInsetTop + initialState.margins.top
            }
        }.applyToView(binding.toolbar)

        Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
            view.apply {
                val leftSpace = insets.systemWindowInsetLeft + initialState.paddings.left
                updatePadding(left = leftSpace)
                updateLayoutParams {
                    if (getWidth() > 0) {
                        width = measuredWidth + leftSpace
                    }
                }
            }
        }.applyToView(binding.navigationView)

        binding.navigationView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            if (navController.currentDestination?.id == it.itemId) return@setNavigationItemSelectedListener false
            consume {
                val builder = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.homeFragment, false)
                    .setEnterAnim(R.anim.nav_default_enter_anim)
                    .setExitAnim(R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
                val options = builder.build()
                navController.navigate(it.itemId, null, options)
            }
        }

        binding.toolbar.doOnLayout {
            invalidateOptionsMenu()
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
        binding.navigationView.menu.findItem(destination.id)?.isChecked = true

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
                binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_menu_black_24dp)
                supportActionBar?.title = "Info"
            }
            R.id.settingFragment -> {
                binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_menu_black_24dp)
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
