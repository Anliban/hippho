package com.anliban.team.hippho.domain.detail

import androidx.lifecycle.MutableLiveData
import com.anliban.team.hippho.di.qualifier.DefaultDispatcher
import com.anliban.team.hippho.domain.UseCase
import com.anliban.team.hippho.ui.detail.DetailImage
import com.anliban.team.hippho.ui.detail.OrganizeImage
import kotlinx.coroutines.CoroutineDispatcher

class ScaleImageAnimLiveDataUseCase(
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : UseCase<ScaleImageAnimRequestParameters, List<DetailImage>>(defaultDispatcher) {

    override suspend fun execute(parameters: ScaleImageAnimRequestParameters): List<DetailImage> {
        return when (parameters.type) {
            OrganizeImage.All -> scaleAll(parameters.allState, parameters.items)
            OrganizeImage.Single -> scaleSingle(parameters.clickedId, parameters.items)
        }
    }

    private fun scaleSingle(
        clickedId: MutableLiveData<Long>,
        items: List<DetailImage>?
    ): List<DetailImage> {
        val result = items?.toMutableList() ?: mutableListOf()

        clickedId.value?.let { id ->
            val image = result.find { it.image.id == id } ?: return@let
            val position = result.indexOf(image)

            result[position] =
                result[position].copy(isScaled = !image.isScaled, isSelected = image.isSelected)
        }

        return result
    }

    private fun scaleAll(
        allState: Boolean?,
        items: List<DetailImage>?
    ): List<DetailImage> {
        val status = allState ?: return emptyList()
        val result = items?.toMutableList() ?: mutableListOf()

        return result.map { it.copy(isScaled = !status) }
    }
}

data class ScaleImageAnimRequestParameters(
    val clickedId: MutableLiveData<Long>,
    val items: List<DetailImage>?,
    val type: OrganizeImage,
    val allState: Boolean? = null
)
