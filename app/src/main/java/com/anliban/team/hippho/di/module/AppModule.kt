package com.anliban.team.hippho.di.module

import android.app.Application
import android.content.Context
import com.anliban.team.hippho.HipphoApp
import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.data.MediaProviderImpl
import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.data.pref.SharedPreferenceStorage
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

    @Provides
    fun provideMediaProvider(context: Context): MediaProvider = MediaProviderImpl(context)

    @Singleton
    @Provides
    fun providePreferenceStorage(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)
}
