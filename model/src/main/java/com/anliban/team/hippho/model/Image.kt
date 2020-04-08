package com.anliban.team.hippho.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Image(
    val id: Long,
    val fileName: String,
    val date: Date,
    val contentUri: String
) : Parcelable
