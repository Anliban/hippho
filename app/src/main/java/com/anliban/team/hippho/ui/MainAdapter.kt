package com.anliban.team.hippho.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.databinding.ItemMainBinding

class MainAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel
) : ListAdapter<MainUiModel, MainViewHolder>(MainListDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding, lifecycleOwner, viewModel)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class MainViewHolder(
    private val binding: ItemMainBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: MainUiModel) {
        binding.item = item.item[0]
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }
}

val MainListDiffUtil = object : DiffUtil.ItemCallback<MainUiModel>() {
    override fun areItemsTheSame(oldItem: MainUiModel, newItem: MainUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MainUiModel, newItem: MainUiModel): Boolean {
        return oldItem == newItem
    }

}