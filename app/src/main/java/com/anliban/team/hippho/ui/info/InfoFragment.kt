package com.anliban.team.hippho.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.anliban.team.hippho.databinding.FragmentInfoBinding
import com.anliban.team.hippho.util.viewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class InfoFragment : DaggerFragment() {

    private lateinit var binding: FragmentInfoBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModel<InfoViewModel> { viewModelFactory.create(InfoViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false).apply {
            viewModel = this@InfoFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

}