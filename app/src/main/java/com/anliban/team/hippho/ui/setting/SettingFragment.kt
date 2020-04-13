package com.anliban.team.hippho.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.anliban.team.hippho.databinding.FragmentSettingBinding
import com.anliban.team.hippho.util.viewModel
import dagger.android.support.DaggerFragment
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import javax.inject.Inject

class SettingFragment : DaggerFragment() {

    private lateinit var binding: FragmentSettingBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModel<SettingViewModel> { viewModelFactory.create(SettingViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false).apply {
            viewModel = this@SettingFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.root.doOnApplyWindowInsets { view, insets, initialState ->
            view.updatePadding(
                bottom = insets.systemWindowInsetBottom + initialState.paddings.bottom
            )
        }

        return binding.root
    }
}
