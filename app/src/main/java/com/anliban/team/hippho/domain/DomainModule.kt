package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.ImageLoadHelper
import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.di.qualifier.IoDispatcher
import com.anliban.team.hippho.domain.detail.ScaleImageAnimUseCase
import com.anliban.team.hippho.domain.detail.SwitchImageIndicatorUseCase
import com.anliban.team.hippho.domain.detail.SwitchImagePositionUseCase
import com.anliban.team.hippho.domain.info.LoadInfoDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ApplicationComponent::class)
class DomainModule {

    @Provides
    fun provideGetImageByDateUseCase(
        mediaProvider: MediaProvider,
        imageLoaderHelper: ImageLoadHelper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GetImageByDateUseCase =
        GetImageByDateUseCase(mediaProvider, imageLoaderHelper, ioDispatcher)

    @Provides
    fun provideGetImageByIdUseCase(
        mediaProvider: MediaProvider,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GetImageByIdUseCase = GetImageByIdUseCase(mediaProvider, ioDispatcher)

    @Provides
    fun provideSwitchImagePositionUseCase(): SwitchImagePositionUseCase =
        SwitchImagePositionUseCase()

    @Provides
    fun provideSwitchImageIndicatorUseCase(): SwitchImageIndicatorUseCase =
        SwitchImageIndicatorUseCase()

    @Provides
    fun provideScaleImageAnimUseCase(): ScaleImageAnimUseCase = ScaleImageAnimUseCase()

    @Provides
    fun provideImageLoadHelper(): ImageLoadHelper = ImageLoadHelper()

    @Provides
    fun provideDeleteImageUseCase(
        mediaProvider: MediaProvider,
        preferenceStorage: PreferenceStorage
    ): DeleteImageUseCase = DeleteImageUseCase(mediaProvider, preferenceStorage)

    @Provides
    fun provideLoadInfoDataUseCase(
        preferenceStorage: PreferenceStorage
    ): LoadInfoDataUseCase = LoadInfoDataUseCase(preferenceStorage)
}
