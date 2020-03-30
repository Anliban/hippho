package com.anliban.team.hippho.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.ActivityMainBinding
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

    private var navFragment: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity
        }

      /*  navFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navFragment?.navController*/

       /* navFragment?.let {
            NavigationUI.setupWithNavController(binding.bottomNavigation, it.navController)
        }

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            renderBottom(destination.id)
        }*/
    }
}
