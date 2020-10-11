package com.anliban.team.hippho.domain.info

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anliban.team.hippho.CoroutinesRule
import com.anliban.team.hippho.MockkRule
import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.model.successOr
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class LoadInfoDataUseCaseTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockkRule(this)

    @get:Rule
    val coroutineTestRule = CoroutinesRule()

    lateinit var useCase: LoadInfoDataUseCase

    @MockK(relaxed = true)
    lateinit var preferenceStorage: PreferenceStorage

    @Test
    fun `지운 사진 갯수, 용량 데이터 로드`() {
        useCase = LoadInfoDataUseCase(preferenceStorage, coroutineTestRule.testDispatcher)

        every {
            preferenceStorage.deletedCount
        } returns 0L

        every {
            preferenceStorage.deletedFileSize
        } returns 0L

        coroutineTestRule.testDispatcher.runBlockingTest {
            val result = useCase.invoke(Unit).successOr(null)
            assert(result?.deletedImageCount == 0L)
            assert(result?.deletedFileSize == 0L)
        }
    }
}
