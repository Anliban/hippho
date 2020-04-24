package com.anliban.team.hippho.domain.detail

import androidx.lifecycle.MutableLiveData
import com.anliban.team.hippho.domain.UseCase
import com.anliban.team.hippho.ui.detail.DetailImage

/**
 * Created by choejun-yeong on 24/04/2020.
 */

class SwitchImageIndicatorUseCase :
    UseCase<SwitchImageIndicatorRequestParameters, List<DetailImage>>() {

    override fun execute(parameters: SwitchImageIndicatorRequestParameters): List<DetailImage> {

        val clickedId = parameters.clickedId
        val position = parameters.position
        val result = parameters.items?.toMutableList() ?: mutableListOf()

        if (result[position].isSelected) {
            return parameters.items ?: emptyList()
        }

        result.find { it.isSelected }?.let {
            val originPosition = result.indexOf(it)
            result[originPosition] =
                result[originPosition].copy(isSelected = false, isScaled = it.isScaled)
        }

        result[position] = result[position].copy(isSelected = true)
        clickedId.value = result[position].image.id

        return result
    }
}

data class SwitchImageIndicatorRequestParameters(
    val position: Int,
    val items: List<DetailImage>?,
    val clickedId: MutableLiveData<Long>
)
