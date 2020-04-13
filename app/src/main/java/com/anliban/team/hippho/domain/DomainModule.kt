package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.ImageLoadHelper
import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.domain.detail.ScaleImageAnimUseCase
import com.anliban.team.hippho.domain.detail.SwitchImagePositionUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetImageByDateUseCase(
        mediaProvider: MediaProvider,
        imageLoaderHelper: ImageLoadHelper
    ): GetImageByDateUseCase =
        GetImageByDateUseCase(mediaProvider, imageLoaderHelper)

    @Provides
    fun provideGetImageByIdUseCase(
        mediaProvider: MediaProvider
    ): GetImageByIdUseCase = GetImageByIdUseCase(mediaProvider)

    @Provides
    fun provideSwitchImagePositionUseCase(): SwitchImagePositionUseCase = SwitchImagePositionUseCase()

    @Provides
    fun provideScaleImageAnimUseCase(): ScaleImageAnimUseCase = ScaleImageAnimUseCase()

    @Provides
    fun provideImageLoadHelper(): ImageLoadHelper = ImageLoadHelper()

    @Provides
    fun provideDeleteImageUseCase(mediaProvider: MediaProvider): DeleteImageUseCase = DeleteImageUseCase(mediaProvider)
}
