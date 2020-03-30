package com.anliban.team.hippho.ui.home

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date

@BindingAdapter("homeItems")
fun bindHomeItems(recyclerView: RecyclerView, items: List<HomeUiModel>?) {
    items?.let {
        (recyclerView.adapter as HomeAdapter).submitList(it)
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("bindDate")
fun TextView.bindDate(date: Date?) {
    date?.let {
        val format = SimpleDateFormat("yyyy.MM.dd")
        text = format.format(it)
    }
}