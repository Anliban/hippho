package com.anliban.team.hippho.ui.home

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.FragmentHomeBinding
import com.anliban.team.hippho.ui.home.adapter.HomeAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableMyLocation(true)
        // requestPermissions()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_READ_STORAGE_PERMISSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.loadImages()
            } else {
                ReadStorageRationaleFragment()
                        .show(childFragmentManager, FRAGMENT_READ_STORAGE)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun enableMyLocation(requestPermission: Boolean = false) {
        val context = context ?: return
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.loadImages()
            }
            requestPermission -> requestLocationPermission()
            else -> {
            }
        }
    }

    private fun requestLocationPermission() {
        val context = context ?: return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ReadStorageRationaleFragment()
                    .show(childFragmentManager, FRAGMENT_READ_STORAGE)
            return
        }
        requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_STORAGE_PERMISSION
        )
    }

    class ReadStorageRationaleFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.permission_string2)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        requireParentFragment().requestPermissions(
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                REQUEST_READ_STORAGE_PERMISSION
                        )
                    }
                    .setNegativeButton(android.R.string.cancel, null) // Give up
                    .create()
        }
    }

    companion object {
        private const val EXT_REFRESH = "refreshing"
        private const val FRAGMENT_READ_STORAGE = "readStorage"
        private const val REQUEST_READ_STORAGE_PERMISSION = 1
    }
}
