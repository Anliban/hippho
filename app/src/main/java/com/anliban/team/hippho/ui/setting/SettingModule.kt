package com.anliban.team.hippho.ui.setting

import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.di.annotations.FragmentScoped
import com.anliban.team.hippho.di.annotations.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class SettingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributesSettingFragment(): SettingFragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    internal abstract fun bindViewModel(viewModel: SettingViewModel): ViewModel
}
