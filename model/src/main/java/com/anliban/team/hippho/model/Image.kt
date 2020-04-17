package com.anliban.team.hippho.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

/**
 *  @param fileSize : MB 단위.
 * */

@Parcelize
data class Image(
    val id: Long,
    val fileName: String,
    val date: Date,
    val contentUri: String,
    val fileSize: Long
) : Parcelable
