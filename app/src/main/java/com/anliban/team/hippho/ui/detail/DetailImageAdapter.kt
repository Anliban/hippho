package com.anliban.team.hippho.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.databinding.ItemDetailThumbnailBinding
import com.anliban.team.hippho.databinding.ItemDetailSecondBinding
import com.anliban.team.hippho.util.scaleRevert
import com.anliban.team.hippho.util.scaleStart
import java.lang.IllegalStateException

class DetailImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: DetailViewModel,
    private val listType: DetailListType
) : ListAdapter<DetailUiModel, DetailImageViewHolder<DetailUiModel>>(
    DetailImageDiffUtil
) {

    private companion object {
        private const val VIEW_TYPE_THUMBS = 0
        private const val VIEW_TYPE_SECOND = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (listType) {
            DetailListType.Thumb -> VIEW_TYPE_THUMBS
            DetailListType.Second -> VIEW_TYPE_SECOND
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailImageViewHolder<DetailUiModel> {
        return when (viewType) {
            VIEW_TYPE_THUMBS -> {
                val binding = ItemDetailThumbnailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DetailThumbNailViewHolder(binding, lifecycleOwner)
            }
            VIEW_TYPE_SECOND -> {
                val binding =
                    ItemDetailSecondBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                DetailSecondViewHolder(binding, lifecycleOwner, viewModel)
            }
            else -> throw IllegalStateException("Invalid View Type!")
        }
    }

    override fun onBindViewHolder(holder: DetailImageViewHolder<DetailUiModel>, position: Int) {
        holder.onBind(getItem(position))
    }
}

class DetailThumbNailViewHolder(
    private val binding: ItemDetailThumbnailBinding,
    private val viewLifecycleOwner: LifecycleOwner
) : DetailImageViewHolder<DetailUiModel>(binding.root) {

    override fun onBind(item: DetailUiModel) {
        binding.item = item.image
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
    }
}

class DetailSecondViewHolder(
    private val binding: ItemDetailSecondBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: DetailViewModel
) : DetailImageViewHolder<DetailUiModel>(binding.root) {

    override fun onBind(item: DetailUiModel) {
        require(item is DetailImage) { "Invalid Type" }
        binding.item = item
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        if (item.isScaled) {
            scaleStart(binding.image) {
                binding.filter.isVisible = true
            }
        } else {
            scaleRevert(binding.image) {
                binding.filter.isVisible = false
            }
        }
    }
}

sealed class DetailImageViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(item: T)
}

val DetailImageDiffUtil = object : DiffUtil.ItemCallback<DetailUiModel>() {
    override fun areItemsTheSame(oldItem: DetailUiModel, newItem: DetailUiModel): Boolean {
        return oldItem.image.id == newItem.image.id
    }

    override fun areContentsTheSame(oldItem: DetailUiModel, newItem: DetailUiModel): Boolean {
        return oldItem == newItem
    }
}
