package com.anliban.team.hippho.ui

import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.di.annotations.FragmentScoped
import com.anliban.team.hippho.di.annotations.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
@Suppress("UNUSED")
abstract class MainActivityModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): MainFragment

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindViewModel(viewModel: MainViewModel): ViewModel
}