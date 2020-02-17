package com.anliban.team.hippho.di.module

import android.app.Application
import android.content.Context
import com.anliban.team.hippho.HipphoApp

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: HipphoApp): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideApplication(): Application = HipphoApp()
}