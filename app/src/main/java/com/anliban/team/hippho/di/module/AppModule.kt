package com.anliban.team.hippho.di.module

import android.app.Application
import android.content.Context
import com.anliban.team.hippho.HipphoApp
import com.anliban.team.hippho.core.ImageSimilarFinder
import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.data.ImageLoaderImpl

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
    fun provideImageLoader(context: Context): ImageLoader = ImageLoaderImpl(context)

    @Provides
    fun provideImageSimilarFinder(): ImageSimilarFinder = ImageSimilarFinder()
}