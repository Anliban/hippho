package com.anliban.team.hippho.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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

@SuppressLint("SimpleDateFormat")
fun dateToTimestamp(day: Int, month: Int, year: Int): Long {
    return SimpleDateFormat("dd.MM.yyyy").let { formatter ->
        formatter.parse("$day.$month.$year")?.time ?: 0
    }
}
