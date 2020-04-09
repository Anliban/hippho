package com.anliban.team.hippho.data

import com.anliban.team.hippho.model.Image
import org.joda.time.DateTime
import org.joda.time.Seconds
import java.util.Date
import kotlin.math.abs

/**
 * Created by choejun-yeong on 09/04/2020.
 */

private typealias GroupingResult = Pair<Date, List<Image>>

class ImageLoadHelper {

    private companion object {
        const val intervalCriterion = 60 // 초
    }

    fun groupByTimeInterval(date: Date, images: List<Image>): List<GroupingResult> {

        val result = mutableListOf<GroupingResult>()
        val group = mutableListOf<Image>()

        images.forEachIndexed imageLoop@{ index, image ->
            if (index == 0) {
                group.add(image)
            }

            val targetPosition = index + 1

            if (targetPosition > images.lastIndex) {
                result.add(date to group.toList())
                group.clear()
                return@imageLoop
            }

            val target = images[targetPosition]
            val isSameGroup = compareTime(image.date, target.date)

            if (isSameGroup) {
                group.add(target)
            } else {
                result.add(date to group.toList())
                group.clear()
                group.add(target)
            }
            date to group.toList()
        }

        return result
    }

    private fun compareTime(comp1: Date, comp2: Date): Boolean {
        val date1 = DateTime(comp1)
        val date2 = DateTime(comp2)

        val result = Seconds.secondsBetween(date1, date2)

        return abs(result.seconds) < intervalCriterion
    }
}
