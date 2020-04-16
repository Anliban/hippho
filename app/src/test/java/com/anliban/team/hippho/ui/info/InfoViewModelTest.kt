package com.anliban.team.hippho.ui.info

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anliban.team.hippho.MockkRule
import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.domain.info.LoadInfoDataResult
import com.anliban.team.hippho.domain.info.LoadInfoDataUseCase
import com.anliban.team.hippho.getOrAwaitValue
import com.anliban.team.hippho.util.bytesToMegaBytes
import com.anliban.team.hippho.util.toDecimal
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class InfoViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockkRule(this)

    lateinit var viewModel: InfoViewModel

    @Test
    fun `지운사진 및 이미지 데이터 로드`() {

        val infoDataResult = LoadInfoDataResult(2, 2048)
        viewModel = createViewModel()

        assert(
            viewModel.deletedMemoryCount.getOrAwaitValue() ==
                    infoDataResult.deletedFileSize.bytesToMegaBytes().toDecimal()
        )

        assert(
            viewModel.deletedPhotoCount.getOrAwaitValue() ==
                    infoDataResult.deletedImageCount.toDouble().toDecimal()
        )
    }

    private fun createViewModel(): InfoViewModel {
        val preferenceStorage = mockk<PreferenceStorage>(relaxed = true)
        every {
            preferenceStorage.deletedCount
        } returns 2

        every {
            preferenceStorage.deletedFileSize
        } returns 2048
        return InfoViewModel(
            loadInfoDataUseCase = LoadInfoDataUseCase(preferenceStorage)
        )
    }
}
