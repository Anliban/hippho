package com.anliban.team.hippho.domain
import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.domain.model.GetImageRequestParameters
import com.anliban.team.hippho.ui.home.HomeListContent
import com.anliban.team.hippho.ui.home.HomeListHeader
import com.anliban.team.hippho.ui.home.HomeUiModel
import com.anliban.team.hippho.util.midNight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetImageByDateUseCase(
    private val imageLoader: ImageLoader
) : FlowUseCase<GetImageRequestParameters, List<HomeUiModel>>() {

    override fun execute(parameters: GetImageRequestParameters): Flow<List<HomeUiModel>> {
        return imageLoader.getImages(parameters.option)
            .map { images ->
                images.groupBy { it.date.midNight() }
                    .flatMap {
                        val header = HomeListHeader(it.key)
                        val content = HomeListContent(it.value)
                        listOf(header) + content
                    }
            }
    }
}
