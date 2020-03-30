package com.anliban.team.hippho.di.module

import com.anliban.team.hippho.di.annotations.ActivityScoped
import com.anliban.team.hippho.ui.MainActivity
import com.anliban.team.hippho.ui.MainActivityModule
import com.anliban.team.hippho.ui.detail.DetailModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            DetailModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}