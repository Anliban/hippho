package com.anliban.team.hippho.ui.home

import com.anliban.team.hippho.model.Image
import java.util.Date

sealed class HomeUiModel

data class HomeListHeader(
    val date: Date
) : HomeUiModel()


data class HomeListContent(
    val data: List<Image>
) : HomeUiModel()