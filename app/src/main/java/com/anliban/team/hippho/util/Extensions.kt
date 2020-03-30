package com.anliban.team.hippho.util

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> ComponentActivity.activityViewModel(
    crossinline body: () -> T
): Lazy<T> {
    return viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return body() as T
            }
        }
    }
}

inline fun <reified T : ViewModel> Fragment.viewModel(
    crossinline body: () -> T
): Lazy<T> {
    return viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return body() as T
            }
        }
    }
}

inline fun <reified T : ViewModel> Fragment.activityViewModel(
    crossinline body: () -> T
): Lazy<T> {
    return activityViewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return body() as T
            }
        }
    }
}