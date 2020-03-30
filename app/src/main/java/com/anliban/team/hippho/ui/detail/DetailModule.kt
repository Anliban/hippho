package com.anliban.team.hippho.ui.detail

import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.di.annotations.FragmentScoped
import com.anliban.team.hippho.di.annotations.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class DetailModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributesDetailFragment(): DetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun bindViewModel(viewModel: DetailViewModel): ViewModel
}