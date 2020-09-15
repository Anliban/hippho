package com.anliban.team.hippho.ui.home

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.FragmentHomeBinding
import com.anliban.team.hippho.ui.home.adapter.HomeAdapter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
    }

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
            adapter = HomeAdapter(viewLifecycleOwner) { ids, _ ->
                findNavController().navigate(
                    HomeFragmentDirections.actionToDetail(ids.toLongArray())
                    // , FragmentNavigatorExtras(sharedElement)
                )
            }
        }

        Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
            view.updatePadding(
                    bottom = insets.systemWindowInsetBottom + initialState.paddings.bottom
            )
        }.applyToView(binding.recyclerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry
            ?.savedStateHandle?.getLiveData<Boolean>(EXT_REFRESH)
            ?.observe(viewLifecycleOwner, Observer {
                if (it) {
                    viewModel.onSwipeRefresh()
                }
            })
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
            .setPermissions(*getRequestPermission())
            .check()
    }

    private fun getRequestPermission(): Array<String> {
        val result = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            result + Manifest.permission.ACCESS_MEDIA_LOCATION
        }

        return result
    }

    private companion object {
        private const val EXT_REFRESH = "refreshing"
    }
}
