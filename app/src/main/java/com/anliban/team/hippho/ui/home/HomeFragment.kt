package com.anliban.team.hippho.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.FragmentHomeBinding
import com.anliban.team.hippho.ui.home.adapter.HomeAdapter
import com.anliban.team.hippho.util.viewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModel { viewModelFactory.create(HomeViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewModel = this@HomeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.recyclerView.apply {
            adapter = HomeAdapter(viewLifecycleOwner) {
                navigateToDetail(it)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
    }

    private fun requestPermissions() {
        TedPermission.with(requireContext())
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    viewModel.loadImages()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    requireActivity().finish()
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

    private fun navigateToDetail(item: HomeUiModel) {

    }
}