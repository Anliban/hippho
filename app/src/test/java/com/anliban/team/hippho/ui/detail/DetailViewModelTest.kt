package com.anliban.team.hippho.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.anliban.team.hippho.CoroutinesRule
import com.anliban.team.hippho.DummyData
import com.anliban.team.hippho.MockkRule
import com.anliban.team.hippho.data.ImageQueryOption
import com.anliban.team.hippho.domain.DeleteImageUseCase
import com.anliban.team.hippho.domain.GetImageByIdUseCase
import com.anliban.team.hippho.domain.detail.ScaleImageAnimUseCase
import com.anliban.team.hippho.domain.detail.SwitchImagePositionRequestParameters
import com.anliban.team.hippho.domain.detail.SwitchImagePositionUseCase
import com.anliban.team.hippho.domain.model.GetImageRequestParameters
import com.anliban.team.hippho.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockkRule(this)

    @get:Rule
    val coroutineTestRule = CoroutinesRule()

    @MockK(relaxed = true)
    lateinit var getImageByDateUseCase: GetImageByIdUseCase

    @MockK(relaxed = true)
    lateinit var switchImagePositionUseCase: SwitchImagePositionUseCase

    lateinit var scaleImageAnimUseCase: ScaleImageAnimUseCase

    @MockK(relaxed = true)
    lateinit var deleteImageUseCase: DeleteImageUseCase

    private lateinit var viewModel: DetailViewModel

    @Test
    fun `요청한 id값들의 이미지 로드`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val ids = DummyData.imageIds
        val images = DummyData.images

        val request = GetImageRequestParameters(
            option = ImageQueryOption.ID,
            ids = ids.toList()
        )

        viewModel = createViewModel()

        coVerify { getImageByDateUseCase.execute(request) }

        assert(viewModel.thumbnails.getOrAwaitValue() == images.mapDetailUiModel())
        assert(viewModel.secondLists.getOrAwaitValue() == images.mapDetailImageList())
    }

    @Test
    fun `가로 이미지 리스트 아이템 클릭시, 썸네일 포지션 이동`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val images = DummyData.images
        val selectedImage = DetailImage(images[1])

        val mockSelectedImage = MutableLiveData(images.mapDetailImageList())
        val mockClickedId = MutableLiveData(images[0].id)
        val selectedImageRequest = SwitchImagePositionRequestParameters(
            model = selectedImage,
            items = images.mapDetailImageList(),
            clickedId = mockClickedId
        )

        every {
            switchImagePositionUseCase(selectedImageRequest, mockSelectedImage)
        } just runs

        viewModel = createViewModel()

        viewModel.thumbnails.observeForever { }
        viewModel.clickToSecondItem(selectedImage)

        val thumbnail = viewModel.thumbnails.getOrAwaitValue()
        val position = thumbnail.indexOf(thumbnail.find { it.image == selectedImage.image })

        assert(viewModel.moveToThumbnail.getOrAwaitValue().peekContent() == position)
    }

    @Test
    fun `이미지 선택`() = coroutineTestRule.testDispatcher.runBlockingTest {

        viewModel = createViewModel()
        viewModel.secondLists.observeForever { }
        viewModel.selectImage()

        assert(viewModel.secondLists.getOrAwaitValue() == DummyData.firstItemSelectedImages)
    }

    @Test
    fun `선택된 이미지 취소`() = coroutineTestRule.testDispatcher.runBlockingTest {

        viewModel = createViewModel()
        viewModel.secondLists.observeForever { }

        // selected
        viewModel.selectImage()

        // unselected
        viewModel.selectImage()

        assert(viewModel.secondLists.getOrAwaitValue() == DummyData.unSelectedImages)
    }

    @Test
    fun `모든 이미지 선택취소`() = coroutineTestRule.testDispatcher.runBlockingTest {

        viewModel = createViewModel()
        viewModel.secondLists.observeForever { }
        viewModel.organizeImagesState.observeForever { }

        // selected
        viewModel.selectImages()

        // unselected
        viewModel.selectImages()

        assert(viewModel.secondLists.getOrAwaitValue() == DummyData.unSelectedImages)
    }

    @Test
    fun `모든 이미지 선택`() = coroutineTestRule.testDispatcher.runBlockingTest {

        viewModel = createViewModel()
        viewModel.secondLists.observeForever { }
        viewModel.organizeImagesState.observeForever { }
        viewModel.selectImages()

        assert(viewModel.secondLists.getOrAwaitValue() == DummyData.allSelectedImages)
    }

    @Test
    fun `선택된 이미지가 없을 경우, snackbar가 호출되어야 한다`() = coroutineTestRule.testDispatcher.runBlockingTest {
        viewModel = createViewModel()
        viewModel.secondLists.observeForever { }

        viewModel.requestDeleteImages()

        assert(viewModel.showEmptySelected.getOrAwaitValue().peekContent() == Unit)
    }

    @Test
    fun `선택된 이미지가 있는 경우, Dialog가 호출되어야 한다`() = coroutineTestRule.testDispatcher.runBlockingTest {
        viewModel = createViewModel()
        viewModel.secondLists.observeForever { }

        viewModel.selectImage() // or viewModel.selectImage()
        viewModel.requestDeleteImages()

        assert(viewModel.requestDeleteImage.getOrAwaitValue().peekContent() == Unit)
    }

    @Test
    fun `이미지 삭제`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val images = DummyData.firstItemSelectedImages
            .filter { it.isScaled }
            .map { it.image }

        coEvery {
            deleteImageUseCase.execute(images)
        } just runs

        viewModel = createViewModel()
        viewModel.secondLists.observeForever { }

        viewModel.selectImage()
        viewModel.deleteImages()

        coVerify {
            deleteImageUseCase.execute(images)
        }

        val isClickedNavigateToHome =
            viewModel.navigateToHome.getOrAwaitValue().peekContent() == Unit
        assert(isClickedNavigateToHome)
    }

    private fun createViewModel(): DetailViewModel {
        val ids = DummyData.imageIds

        val request = GetImageRequestParameters(
            option = ImageQueryOption.ID,
            ids = DummyData.imageIds.toList()
        )

        coEvery {
            getImageByDateUseCase.execute(request)
        } returns flowOf(DummyData.images)

        scaleImageAnimUseCase = ScaleImageAnimUseCase()

        return DetailViewModel(
            getImageByDateUseCase,
            switchImagePositionUseCase,
            scaleImageAnimUseCase,
            deleteImageUseCase,
            ids
        )
    }
}
