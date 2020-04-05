package com.anliban.team.hippho.ui.home

import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.di.annotations.FragmentScoped
import com.anliban.team.hippho.di.annotations.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class HomeModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributesHomeFragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel
}