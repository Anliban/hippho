package com.anliban.team.hippho.domain

import com.anliban.team.hippho.core.ImageSimilarFinder
import com.anliban.team.hippho.data.ImageLoader
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetImageByDateUseCase(
        imageLoader: ImageLoader,
        imageSimilarFinder: ImageSimilarFinder
    ): GetImageByDateUseCase =
        GetImageByDateUseCase(imageLoader, imageSimilarFinder)
}