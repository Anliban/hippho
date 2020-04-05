package com.anliban.team.hippho.di.component

import com.anliban.team.hippho.HipphoApp
import com.anliban.team.hippho.di.module.ActivityBindingModule
import com.anliban.team.hippho.di.module.AppModule
import com.anliban.team.hippho.di.module.ViewModelModule
import com.anliban.team.hippho.domain.DomainModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class,
        DomainModule::class
    ]
)
interface AppComponent : AndroidInjector<HipphoApp> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: HipphoApp): AppComponent
    }
}