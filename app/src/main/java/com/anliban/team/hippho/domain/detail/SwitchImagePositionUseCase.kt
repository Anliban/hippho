package com.anliban.team.hippho.domain.detail

import androidx.lifecycle.MutableLiveData
import com.anliban.team.hippho.domain.UseCase
import com.anliban.team.hippho.ui.detail.DetailImage

class SwitchImagePositionUseCase :
    UseCase<SwitchImagePositionRequestParameters, List<DetailImage>>() {

    override fun execute(parameters: SwitchImagePositionRequestParameters): List<DetailImage> {

        val clickedId = parameters.clickedId
        val model = parameters.model

        if (clickedId.value == model.image.id) {
            return parameters.items ?: emptyList()
        }

        val result = parameters.items?.toMutableList() ?: mutableListOf()


        clickedId.value?.let { id ->
            val image = result.find { it.image.id == id } ?: return@let
            val position = result.indexOf(image)
            result[position] = result[position].copy(isSelected = false, isScaled = image.isScaled)
        }

        val position = result.indexOf(model)
        val item = model.copy(isSelected = true, isScaled = model.isScaled)
        result[position] = item
        clickedId.value = item.image.id

        return result
    }
}

data class SwitchImagePositionRequestParameters(
    val model: DetailImage,
    val items: List<DetailImage>?,
    val clickedId: MutableLiveData<Long>
)