package com.anliban.team.hippho.ui.detail

import com.anliban.team.hippho.di.annotations.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class DetailModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [DetailAssistedInjectModule::class])
    internal abstract fun contributesDetailFragment(): DetailFragment
}
