package com.anliban.team.hippho.ui.detail

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.model.Event

@BindingAdapter("bindDetailImages")
fun RecyclerView.bindDetailImages(items: List<DetailUiModel>?) {
    items?.let {
        (adapter as DetailImageAdapter).submitList(it)
    }
}

@BindingAdapter("moveToItem")
fun RecyclerView.moveToItem(position: Event<Int>?) {
    val movePosition = position?.getContentIfNotHandled() ?: return
    smoothScrollToPosition(movePosition)
}