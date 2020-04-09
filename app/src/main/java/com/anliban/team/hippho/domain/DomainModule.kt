package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.ImageLoadHelper
import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.domain.detail.ScaleImageAnimUseCase
import com.anliban.team.hippho.domain.detail.SwitchImagePositionUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetImageByDateUseCase(
        imageLoader: ImageLoader,
        imageLoaderHelper: ImageLoadHelper
    ): GetImageByDateUseCase =
        GetImageByDateUseCase(imageLoader, imageLoaderHelper)

    @Provides
    fun provideGetImageByIdUseCase(
        imageLoader: ImageLoader
    ): GetImageByIdUseCase = GetImageByIdUseCase(imageLoader)

    @Provides
    fun provideSwitchImagePositionUseCase(): SwitchImagePositionUseCase = SwitchImagePositionUseCase()

    @Provides
    fun provideScaleImageAnimUseCase(): ScaleImageAnimUseCase = ScaleImageAnimUseCase()

    @Provides
    fun provideImageLoadHelper(): ImageLoadHelper = ImageLoadHelper()
}
