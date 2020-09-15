package com.anliban.team.hippho.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anliban.team.hippho.databinding.FragmentInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter

@AndroidEntryPoint
class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    private val viewModel by viewModels<InfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false).apply {
            viewModel = this@InfoFragment.viewModel
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
