package com.anliban.team.hippho.di

import android.content.Context
import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.data.MediaProviderImpl
import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.data.pref.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    fun provideMediaProvider(@ApplicationContext context: Context): MediaProvider = MediaProviderImpl(context)

    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)
}
