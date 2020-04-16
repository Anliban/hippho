package com.anliban.team.hippho.domain.info

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.anliban.team.hippho.MockkRule
import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.getOrAwaitValue
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Rule
import org.junit.Test

class LoadInfoDataUseCaseTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockkRule(this)

    lateinit var useCase: LoadInfoDataUseCase

    @MockK(relaxed = true)
    lateinit var preferenceStorage: PreferenceStorage

    @Test
    fun `지운 사진 갯수, 용량 데이터 로드`() {
        useCase = LoadInfoDataUseCase(preferenceStorage)

        every {
            preferenceStorage.deletedCount
        } returns 0L

        every {
            preferenceStorage.deletedFileSize
        } returns 0L

        val result = MutableLiveData<LoadInfoDataResult>()

        useCase.invoke(Unit, result)

        assert(result.getOrAwaitValue().deletedImageCount == 0L)
        assert(result.getOrAwaitValue().deletedFileSize == 0L)
    }
}
