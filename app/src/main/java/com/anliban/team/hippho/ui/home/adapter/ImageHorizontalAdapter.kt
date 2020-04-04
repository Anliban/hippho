package com.anliban.team.hippho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.databinding.ItemImageHorizontalBinding
import com.anliban.team.hippho.model.Image

class ImageHorizontalAdapter(
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<Image, ImageHorizontalViewHolder>(
    ImageDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHorizontalViewHolder {
        val binding =
            ItemImageHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHorizontalViewHolder(
            binding,
            lifecycleOwner
        )
    }

    override fun onBindViewHolder(holder: ImageHorizontalViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class ImageHorizontalViewHolder(
    private val binding: ItemImageHorizontalBinding,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: Image) {
        binding.item = item
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
    }
}

val ImageDiffUtil = object : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}
