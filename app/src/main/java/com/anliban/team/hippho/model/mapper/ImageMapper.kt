package com.anliban.team.hippho.model.mapper

import com.anliban.team.hippho.model.Image

fun List<Image>.getIds(): List<Long> = map { it.id }
