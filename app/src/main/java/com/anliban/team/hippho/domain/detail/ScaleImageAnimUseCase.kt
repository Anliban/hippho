package com.anliban.team.hippho.domain.detail

import androidx.lifecycle.MutableLiveData
import com.anliban.team.hippho.domain.UseCase
import com.anliban.team.hippho.ui.detail.DetailImage

class ScaleImageAnimUseCase :
    UseCase<ScaleImageAnimRequestParameters, List<DetailImage>>() {


    override fun execute(parameters: ScaleImageAnimRequestParameters): List<DetailImage> {
        val result = parameters.items?.toMutableList() ?: mutableListOf()
        val clickedId = parameters.clickedId

        clickedId.value?.let { id ->
            val image = result.find { it.image.id == id } ?: return@let
            val position = result.indexOf(image)

            result[position] =
                result[position].copy(isScaled = !image.isScaled, isSelected = image.isSelected)
        }
        return result
    }
}

data class ScaleImageAnimRequestParameters(
    val clickedId: MutableLiveData<Long>,
    val items: List<DetailImage>?
)