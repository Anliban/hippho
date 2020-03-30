package com.anliban.team.hippho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.databinding.ItemHomeBinding
import com.anliban.team.hippho.ui.home.HomeUiModel
import com.anliban.team.hippho.ui.home.HomeViewModel
import com.anliban.team.hippho.util.dp2px

class HomeAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val action: (HomeUiModel) -> Unit
) : ListAdapter<HomeUiModel, HomeViewHolder>(
    HomeListDiffUtil
) {

    private val recyclerViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(
            binding,
            lifecycleOwner,
            recyclerViewPool
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class HomeViewHolder(
    private val binding: ItemHomeBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val recyclerViewPool: RecyclerView.RecycledViewPool
) : RecyclerView.ViewHolder(binding.root) {

    private companion object {
        private const val IMAGE_MARGIN = 4f
    }

    fun onBind(item: HomeUiModel) {

        binding.recyclerView.apply {
            adapter =
                ImageHorizontalAdapter(
                    lifecycleOwner
                )
            addItemDecoration(ImageMarginItemDecoration(dp2px(itemView.context, IMAGE_MARGIN)))
            setRecycledViewPool(recyclerViewPool)
        }

        binding.item = item
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