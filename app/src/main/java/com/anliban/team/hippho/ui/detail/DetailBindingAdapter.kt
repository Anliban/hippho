package com.anliban.team.hippho.ui.detail

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anliban.team.hippho.R
import com.anliban.team.hippho.model.Event
import com.google.android.material.button.MaterialButton

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

@BindingAdapter(value = ["organizeState", "imageType"], requireAll = true)
fun selectSingleImage(button: MaterialButton, select: Boolean?, type: OrganizeImage) {
    val isSelected = select ?: return

    val selectText: Int
    val unSelectText: Int

    when (type) {
        OrganizeImage.Single -> {
            selectText = R.string.select_image_text
            unSelectText = R.string.un_select_image_text
        }
        OrganizeImage.All -> {
            selectText = R.string.select_all_image_text
            unSelectText = R.string.un_select_all_image_text
        }
    }

    val context = button.context

    if (isSelected) {
        button.text = context.getString(unSelectText)
        button.icon = ContextCompat.getDrawable(button.context, R.drawable.ic_delete_black_24dp)
    } else {
        button.text = context.getText(selectText)
        button.icon = ContextCompat.getDrawable(button.context, R.drawable.ic_check_black_24dp)
    }
}
