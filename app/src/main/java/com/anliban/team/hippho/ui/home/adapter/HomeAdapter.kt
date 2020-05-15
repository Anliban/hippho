package com.anliban.team.hippho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.databinding.ItemHomeContentBinding
import com.anliban.team.hippho.databinding.ItemHomeHeaderBinding
import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.model.mapper.getIds
import com.anliban.team.hippho.ui.home.HomeListContent
import com.anliban.team.hippho.ui.home.HomeListHeader
import com.anliban.team.hippho.ui.home.HomeUiModel
import com.anliban.team.hippho.util.dp2px
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class HomeAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val action: (List<Long>, Pair<View, String>) -> Unit
) : ListAdapter<HomeUiModel, HomeViewHolder>(
    HomeListDiffUtil
) {

    private companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_CONTENT = 1
    }

    private val recyclerViewPool = RecyclerView.RecycledViewPool()

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeListHeader -> VIEW_TYPE_HEADER
            is HomeListContent -> VIEW_TYPE_CONTENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemHomeHeaderBinding.inflate(
                    (LayoutInflater.from(parent.context)),
                    parent,
                    false
                )
                return HomeListHeaderViewHolder(binding, lifecycleOwner)
            }
            VIEW_TYPE_CONTENT -> {
                val binding = ItemHomeContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HomeListContentViewHolder(
                    binding, lifecycleOwner, recyclerViewPool, action
                )
            }
            else -> throw IllegalStateException("Invalid View Type")
        }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        when (val model = getItem(position)) {
            is HomeListHeader -> (holder as HomeListHeaderViewHolder).onBind(model)
            is HomeListContent -> (holder as HomeListContentViewHolder).onBind(model)
        }
    }
}

class HomeListHeaderViewHolder(
    private val binding: ItemHomeHeaderBinding,
    private val lifecycleOwner: LifecycleOwner
) : HomeViewHolder(binding.root) {

    fun onBind(item: HomeListHeader) {
        binding.item = item.date
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }
}

class HomeListContentViewHolder(
    private val binding: ItemHomeContentBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val recyclerViewPool: RecyclerView.RecycledViewPool,
    private val action: (List<Long>, Pair<View, String>) -> Unit
) : HomeViewHolder(binding.root) {

    private companion object {
        private const val IMAGE_MARGIN = 4f
    }

    fun onBind(item: HomeListContent) {
        binding.recyclerView.apply {
            adapter = ImageHorizontalAdapter(lifecycleOwner)
            if (itemDecorationCount > 0) {
                for (i in itemDecorationCount - 1 downTo 0) {
                    removeItemDecorationAt(i)
                }
            }
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

    private fun navigateToDetail(uiModel: HomeListContent) {
        action(uiModel.data.getIds(), createSharedElements(uiModel.data[0]))
    }

    private fun createSharedElements(image: Image): Pair<View, String> {
        return itemView.run {
            val imageView = binding.recyclerView.getChildAt(0) as AppCompatImageView?

            imageView?.let {
                return@run it to image.contentUri
            }
        } ?: throw IllegalArgumentException("Can not find ImageView")
    }
}

abstract class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view)

val HomeListDiffUtil = object : DiffUtil.ItemCallback<HomeUiModel>() {
    override fun areItemsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean {
        return oldItem == newItem
    }
}
