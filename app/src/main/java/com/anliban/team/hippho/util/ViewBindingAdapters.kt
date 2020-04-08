package com.anliban.team.hippho.util

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("bindImage")
fun bindImage(imageView: AppCompatImageView, path: String?) {
    val imagePath = path ?: return

    GlideApp.with(imageView)
        .load(imagePath)
        .into(imageView)
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean?) {
    val isVisible = visible ?: false
    view.isVisible = isVisible
}
