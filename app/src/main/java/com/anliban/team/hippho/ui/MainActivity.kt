package com.anliban.team.hippho.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.ActivityMainBinding
import com.anliban.team.hippho.ui.home.HomeViewModel
import com.anliban.team.hippho.util.activityViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

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

        binding.toolbar.doOnLayout {
            invalidateOptionsMenu()
        }

        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            onDestinationChanged(destination)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun onDestinationChanged(destination: NavDestination) {
        when (destination.id) {
            R.id.mainFragment -> {
                supportActionBar?.title = "Hippo"
                binding.toolbar.navigationIcon = null
            }
            R.id.detailFragment -> {
                supportActionBar?.title = "Detail"
                binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_black_24dp)
            }
        }
    }
}
