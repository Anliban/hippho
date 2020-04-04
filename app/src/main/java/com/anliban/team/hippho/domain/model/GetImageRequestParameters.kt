package com.anliban.team.hippho.domain.model

import com.anliban.team.hippho.data.ImageQueryOption

data class GetImageRequestParameters(
    val option: ImageQueryOption,
    val ids: List<Long>? = null
)
