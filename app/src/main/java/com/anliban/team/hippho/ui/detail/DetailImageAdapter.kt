package com.anliban.team.hippho.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.databinding.ItemDetailThumbnailBinding
import com.anliban.team.hippho.databinding.ItemDetailSecondBinding
import com.anliban.team.hippho.model.Image
import java.lang.IllegalStateException

class DetailImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: DetailViewModel,
    private val listType: DetailListType
) : ListAdapter<Image, DetailImageViewHolder<Image>>(
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
    ): DetailImageViewHolder<Image> {
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

    override fun onBindViewHolder(holder: DetailImageViewHolder<Image>, position: Int) {
        holder.onBind(getItem(position))
    }
}

class DetailThumbNailViewHolder(
    private val binding: ItemDetailThumbnailBinding,
    private val viewLifecycleOwner: LifecycleOwner
) : DetailImageViewHolder<Image>(binding.root) {

    override fun onBind(item: Image) {
        binding.item = item
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
    }
}

class DetailSecondViewHolder(
    private val binding: ItemDetailSecondBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: DetailViewModel
) : DetailImageViewHolder<Image>(binding.root) {

    override fun onBind(item: Image) {
        binding.item = item
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
    }
}

abstract class DetailImageViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(item: T)
}

val DetailImageDiffUtil = object : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.fileName == newItem.fileName
    }
}