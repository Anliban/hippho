package com.anliban.team.hippho.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Image(
    val id: Long,
    val path: String,
    val date: Date,
    val name: String,
    val absolutePath: String
): Parcelable