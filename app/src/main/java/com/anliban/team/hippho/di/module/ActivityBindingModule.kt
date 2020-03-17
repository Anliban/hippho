package com.anliban.team.hippho.di.module

import com.anliban.team.hippho.di.annotations.ActivityScoped
import com.anliban.team.hippho.ui.main.MainActivity
import com.anliban.team.hippho.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class]
    )
    internal abstract fun mainActivity(): MainActivity

}