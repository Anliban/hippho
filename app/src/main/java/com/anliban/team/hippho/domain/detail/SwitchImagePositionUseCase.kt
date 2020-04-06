package com.anliban.team.hippho.domain.detail

import com.anliban.team.hippho.domain.UseCase
import com.anliban.team.hippho.ui.detail.DetailImage

class SwitchImagePositionUseCase :
    UseCase<SwitchImagePositionRequestParameters, List<DetailImage>>() {

    private var clickedImage: DetailImage? = null

    override fun execute(parameters: SwitchImagePositionRequestParameters): List<DetailImage> {
        val result = parameters.items?.toMutableList() ?: mutableListOf()
        val model = parameters.model

        clickedImage?.let {
            val position = parameters.items?.indexOf(it) ?: return@let
            result[position] = it.copy(isSelected = false)
        }

        val position = result.indexOf(model)
        result[position] = model.copy(isSelected = !model.isSelected)
        clickedImage = result[position]

        return result
    }
}

data class SwitchImagePositionRequestParameters(
    val model: DetailImage,
    val items: List<DetailImage>?
)