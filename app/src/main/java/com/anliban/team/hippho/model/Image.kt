package com.anliban.team.hippho.model

import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Image(
    val id: Long,
    val fileName: String,
    val date: Date,
    val contentUri: String,
    val absolutePath: String
) : Parcelable