package com.anliban.team.hippho.util

import java.text.NumberFormat

/**
 * Created by choejun-yeong on 14/04/2020.
 */

fun Number.bytesToMegaBytes(): Double {
    return toDouble() / 1024 / 1024
}

fun Number.toDecimal(): String = NumberFormat.getInstance().format(this)
