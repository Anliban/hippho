package com.anliban.team.hippho.ui.detail

import com.anliban.team.hippho.model.Image

sealed class DetailUiModel {
    abstract val image: Image
}

data class DetailThumbnail(
    override val image: Image
) : DetailUiModel()

data class DetailImage(
    override val image: Image,
    val isSelected: Boolean = false,
    val isScaled: Boolean = false
) : DetailUiModel()

enum class OrganizeImage {
    Single, All
}

fun Image.mapDetailUiModel() = DetailThumbnail(this)

fun List<Image>.mapDetailUiModel() = map { it.mapDetailUiModel() }

fun Image.mapDetailImage() = DetailImage(image = this)

fun List<Image>.mapDetailImageList() = mapIndexed { index, image ->
    if (index == 0) {
        DetailImage(image = image, isSelected = true)
    } else {
        image.mapDetailImage()
    }
}
