package com.anliban.team.hippho.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bindImage")
fun bindImage(imageView: AppCompatImageView, path: String?) {
    val imagePath = path ?: return

    GlideApp.with(imageView)
        .load(imagePath)
        .into(imageView)
}