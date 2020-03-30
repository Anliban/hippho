package com.anliban.team.hippho.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.databinding.ItemHomeBinding

class HomeAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: HomeViewModel
) : ListAdapter<HomeUiModel, HomeViewHolder>(
    HomeListDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(
            binding,
            lifecycleOwner,
            viewModel
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class HomeViewHolder(
    private val binding: ItemHomeBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: HomeViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: HomeUiModel) {
        binding.item = item.item[0]
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }
}

val HomeListDiffUtil = object : DiffUtil.ItemCallback<HomeUiModel>() {
    override fun areItemsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean {
        return oldItem == newItem
    }

}