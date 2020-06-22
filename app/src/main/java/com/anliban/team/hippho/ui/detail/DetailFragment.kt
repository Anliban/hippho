package com.anliban.team.hippho.ui.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.anliban.team.hippho.R
import com.anliban.team.hippho.databinding.FragmentDetailBinding
import com.anliban.team.hippho.model.EventObserver
import com.anliban.team.hippho.ui.home.adapter.ImageMarginItemDecoration
import com.anliban.team.hippho.util.attachSnapHelperWithListener
import com.anliban.team.hippho.util.dp2px
import com.anliban.team.hippho.util.showSnackBar
import com.anliban.team.hippho.widget.OnSnapPositionChangeListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.doOnApplyWindowInsets

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private val viewModel: DetailViewModel by viewModels<DetailViewModel>()

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

        binding.root.doOnApplyWindowInsets { view, insets, initialState ->
            view.updatePadding(
                bottom = insets.systemWindowInsetBottom + initialState.paddings.bottom
            )
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
                viewModel.requestDeleteImages()
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thumbnailSnapHelper = PagerSnapHelper()
        val onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
            override fun onSnapPositionChange(position: Int) {
                viewModel.changeIndicatorOfSecondList(position)
            }
        }

        binding.thumbnailRecyclerView.apply {
            adapter = DetailImageAdapter(viewLifecycleOwner, viewModel, DetailListType.Thumb)
            attachSnapHelperWithListener(
                thumbnailSnapHelper,
                onSnapPositionChangeListener = onSnapPositionChangeListener
            )
        }

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

        viewModel.showEmptySelected.observe(viewLifecycleOwner, EventObserver {
            showSnackBar(getString(R.string.selected_empty_message))
        })

        viewModel.requestDeleteImage.observe(viewLifecycleOwner, EventObserver {
            RequestDeletedDialogFragment {
                viewModel.deleteImages()
            }.show(childFragmentManager, null)
        })
    }

    private companion object {
        private const val EXT_REFRESH = "refreshing"
    }

    class RequestDeletedDialogFragment(private val action: () -> Unit) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return MaterialAlertDialogBuilder(context)
                .setMessage(R.string.request_deleted_images_text)
                .setPositiveButton(android.R.string.ok) { _, _ -> action() }
                .setNegativeButton(android.R.string.cancel, null) // Give up
                .create()
        }
    }
}
