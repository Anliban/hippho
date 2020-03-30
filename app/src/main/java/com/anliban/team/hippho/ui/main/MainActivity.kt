package com.anliban.team.hippho.ui.main

import android.Manifest
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.ActivityMainBinding
import com.anliban.team.hippho.util.activityViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity
        }

        setPermission()
    }

    private fun setPermission() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    viewModel.loadImages()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    finish()
                }

            })
            .setRationaleMessage(resources.getString(R.string.permission_string2))
            .setDeniedMessage(resources.getString(R.string.permission_string1))
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check()
    }
}
