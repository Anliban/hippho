package com.anliban.team.hippho.di.module

import com.anliban.team.hippho.di.annotations.ActivityScoped
import com.anliban.team.hippho.ui.MainActivity
import com.anliban.team.hippho.ui.MainActivityModule
import com.anliban.team.hippho.ui.detail.DetailModule
import com.anliban.team.hippho.ui.home.HomeModule
import com.anliban.team.hippho.ui.info.InfoModule
import com.anliban.team.hippho.ui.setting.SettingModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            DetailModule::class,
            HomeModule::class,
            InfoModule::class,
            SettingModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}