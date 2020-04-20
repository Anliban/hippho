package com.anliban.team.hippho

import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.ui.detail.DetailImage
import java.util.Date

object DummyData {

    val imageIds = longArrayOf(1, 2, 3)

    val images = listOf(
        Image(1, "1_name.png", Date(), "content://storage/dummy/1", 1024L),
        Image(2, "2_name.png", Date(), "content://storage/dummy/2", 1024L),
        Image(3, "3_name.png", Date(), "content://storage/dummy/3", 1024L)
    )

    val firstItemSelectedImages = listOf(
        DetailImage(image = images[0], isScaled = true, isSelected = true),
        DetailImage(image = images[1], isScaled = false, isSelected = false),
        DetailImage(image = images[2], isScaled = false, isSelected = false)
    )

    val unSelectedImages = listOf(
        DetailImage(image = images[0], isScaled = false, isSelected = true),
        DetailImage(image = images[1], isScaled = false, isSelected = false),
        DetailImage(image = images[2], isScaled = false, isSelected = false)
    )

    val allSelectedImages = listOf(
        DetailImage(image = images[0], isScaled = true, isSelected = true),
        DetailImage(image = images[1], isScaled = true, isSelected = false),
        DetailImage(image = images[2], isScaled = true, isSelected = false)
    )
}
