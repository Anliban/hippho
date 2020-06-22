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
import dev.chrisbanes.insetter.doOnApplyWindowInsets

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

        binding.root.doOnApplyWindowInsets { view, insets, initialState ->
            view.updatePadding(
                bottom = insets.systemWindowInsetBottom + initialState.paddings.bottom
            )
        }

        return binding.root
    }
}
