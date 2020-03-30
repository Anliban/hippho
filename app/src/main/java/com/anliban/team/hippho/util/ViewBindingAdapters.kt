package com.anliban.team.hippho.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bindImage")
fun bindImage(imageView: AppCompatImageView, path: String?) {
    val imagePath = path ?: return

    Glide.with(imageView)
        .load(imagePath)
        .into(imageView)
}