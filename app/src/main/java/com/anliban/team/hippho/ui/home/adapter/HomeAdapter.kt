package com.anliban.team.hippho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.databinding.ItemHomeBinding
import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.ui.home.HomeUiModel
import com.anliban.team.hippho.util.dp2px
import java.lang.IllegalArgumentException

class HomeAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val action: (HomeUiModel, Pair<View, String>) -> Unit
) : ListAdapter<HomeUiModel, HomeViewHolder>(
    HomeListDiffUtil
) {

    private val recyclerViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(
            binding,
            lifecycleOwner,
            recyclerViewPool,
            action
        )
    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class HomeViewHolder(
    private val binding: ItemHomeBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val recyclerViewPool: RecyclerView.RecycledViewPool,
    private val action: (HomeUiModel, Pair<View, String>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private companion object {
        private const val IMAGE_MARGIN = 4f
    }

    fun onBind(item: HomeUiModel) {
        binding.recyclerView.apply {
            adapter = ImageHorizontalAdapter(lifecycleOwner)
            addItemDecoration(ImageMarginItemDecoration(dp2px(itemView.context, IMAGE_MARGIN)))
            setRecycledViewPool(recyclerViewPool)
        }

        itemView.setOnClickListener {
            navigateToDetail(item)
        }

        binding.item = item
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }

    private fun navigateToDetail(uiModel: HomeUiModel) {
        action(uiModel, createSharedElements(uiModel.item[0]))
    }

    private fun createSharedElements(image: Image): Pair<View, String> {
        return itemView.run {
            val imageView = binding.recyclerView.getChildAt(0) as AppCompatImageView?

            imageView?.let {
                return@run it to image.path
            }
        } ?: throw IllegalArgumentException("Can not find ImageView")
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