package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.ImageLoadHelper
import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.di.qualifier.DefaultDispatcher
import com.anliban.team.hippho.di.qualifier.IoDispatcher
import com.anliban.team.hippho.domain.image.ScaleImageAnimLiveDataUseCase
import com.anliban.team.hippho.domain.image.SwitchImageIndicatorLiveDataUseCase
import com.anliban.team.hippho.domain.image.SwitchImagePositionLiveDataUseCase
import com.anliban.team.hippho.domain.image.DeleteImageUseCase
import com.anliban.team.hippho.domain.image.GetImageByDateUseCase
import com.anliban.team.hippho.domain.image.GetImageByIdUseCase
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
        GetImageByDateUseCase(
            mediaProvider,
            imageLoaderHelper,
            ioDispatcher
        )

    @Provides
    fun provideGetImageByIdUseCase(
        mediaProvider: MediaProvider,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GetImageByIdUseCase =
        GetImageByIdUseCase(
            mediaProvider,
            ioDispatcher
        )

    @Provides
    fun provideSwitchImagePositionUseCase(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): SwitchImagePositionLiveDataUseCase =
        SwitchImagePositionLiveDataUseCase(
            defaultDispatcher
        )

    @Provides
    fun provideSwitchImageIndicatorUseCase(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): SwitchImageIndicatorLiveDataUseCase =
        SwitchImageIndicatorLiveDataUseCase(
            defaultDispatcher
        )

    @Provides
    fun provideScaleImageAnimUseCase(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): ScaleImageAnimLiveDataUseCase =
        ScaleImageAnimLiveDataUseCase(
            defaultDispatcher
        )

    @Provides
    fun provideImageLoadHelper(): ImageLoadHelper = ImageLoadHelper()

    @Provides
    fun provideDeleteImageUseCase(
        mediaProvider: MediaProvider,
        preferenceStorage: PreferenceStorage
    ): DeleteImageUseCase =
        DeleteImageUseCase(
            mediaProvider,
            preferenceStorage
        )

    @Provides
    fun provideLoadInfoDataUseCase(
        preferenceStorage: PreferenceStorage,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): LoadInfoDataUseCase = LoadInfoDataUseCase(preferenceStorage, ioDispatcher)
}
