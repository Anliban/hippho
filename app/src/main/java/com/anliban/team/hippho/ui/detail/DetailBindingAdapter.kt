package com.anliban.team.hippho.ui.detail

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.model.Event
import com.anliban.team.hippho.model.Image

@BindingAdapter("bindDetailImages")
fun RecyclerView.bindDetailImages(items: List<Image>?) {
    items?.let {
        (adapter as DetailImageAdapter).submitList(it)
    }
}

@BindingAdapter("moveToItem")
fun RecyclerView.moveToItem(position: Event<Int>?) {
    val movePosition = position?.getContentIfNotHandled() ?: return
    smoothScrollToPosition(movePosition)
}