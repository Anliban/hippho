package com.anliban.team.hippho.ui.info

import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.di.annotations.FragmentScoped
import com.anliban.team.hippho.di.annotations.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class InfoModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributesInfoFragment(): InfoFragment

    @Binds
    @IntoMap
    @ViewModelKey(InfoViewModel::class)
    internal abstract fun bindViewModel(viewModel: InfoViewModel): ViewModel
}
