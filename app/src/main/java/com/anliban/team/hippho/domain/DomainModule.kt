package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.domain.detail.ScaleImageAnimUseCase
import com.anliban.team.hippho.domain.detail.SwitchImagePositionUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetImageByDateUseCase(
        imageLoader: ImageLoader
    ): GetImageByDateUseCase =
        GetImageByDateUseCase(imageLoader)

    @Provides
    fun provideGetImageByIdUseCase(
        imageLoader: ImageLoader
    ): GetImageByIdUseCase = GetImageByIdUseCase(imageLoader)

    @Provides
    fun provideSwitchImagePositionUseCase(): SwitchImagePositionUseCase = SwitchImagePositionUseCase()

    @Provides
    fun provideScaleImageAnimUseCase(): ScaleImageAnimUseCase = ScaleImageAnimUseCase()
}