package com.anliban.team.hippho.ui.home

import android.os.Parcelable
import com.anliban.team.hippho.model.Image
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class HomeUiModel(
    val date: Date,
    val item: List<Image>
) : Parcelable