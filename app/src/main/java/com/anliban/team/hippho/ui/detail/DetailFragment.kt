package com.anliban.team.hippho.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.FragmentDetailBinding
import com.anliban.team.hippho.model.EventObserver
import com.anliban.team.hippho.ui.home.adapter.ImageMarginItemDecoration
import com.anliban.team.hippho.util.dp2px
import com.anliban.team.hippho.util.viewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DetailFragment : DaggerFragment() {

    private lateinit var binding: FragmentDetailBinding

    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: DetailViewModel.Factory

    private val viewModel: DetailViewModel by viewModel {
        viewModelFactory.create(args.images)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            viewModel = this@DetailFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.check -> {
                viewModel.organizeImage()
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thumbnailSnapHelper = LinearSnapHelper()

        binding.thumbnailRecyclerView.apply {
            adapter = DetailImageAdapter(viewLifecycleOwner, viewModel, DetailListType.Thumb)
        }

        thumbnailSnapHelper.attachToRecyclerView(binding.thumbnailRecyclerView)

        binding.secondRecyclerView.apply {
            adapter = DetailImageAdapter(viewLifecycleOwner, viewModel, DetailListType.Second)
            addItemDecoration(ImageMarginItemDecoration(dp2px(requireContext(), 4f)))
            setHasFixedSize(true)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner, EventObserver {
            findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set(EXT_REFRESH, true)
                popBackStack()
            }
        })
    }

    private companion object {
        private const val EXT_REFRESH = "refreshing"
    }
}
