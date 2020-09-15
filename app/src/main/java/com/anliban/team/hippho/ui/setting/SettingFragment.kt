package com.anliban.team.hippho.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anliban.team.hippho.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    private val viewModel by viewModels<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false).apply {
            viewModel = this@SettingFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
            view.updatePadding(
                bottom = insets.systemWindowInsetBottom + initialState.paddings.bottom
            )
        }.applyToView(binding.root)

        return binding.root
    }
}
