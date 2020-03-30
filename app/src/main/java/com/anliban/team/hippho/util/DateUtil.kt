package com.anliban.team.hippho.util

import java.util.*

fun Date.midNight(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@midNight
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.time
}