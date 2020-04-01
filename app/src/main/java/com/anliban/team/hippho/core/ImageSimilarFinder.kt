package com.anliban.team.hippho.core

import com.anliban.team.hippho.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfFloat
import org.opencv.core.MatOfInt
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import timber.log.Timber
import java.util.Date

/**
 *  OpenCv 를 사용해서 이미지를 비교하는 클래스
 *
 * */

private typealias CalculateResult = Pair<Date, List<Image>>

class ImageSimilarFinder {

    init {
        System.loadLibrary(LIB_NAME)
    }

    suspend fun calculate(date: Date, images: List<Image>): List<CalculateResult> {

        val result = mutableListOf<CalculateResult>()

        val latestIndex = images.lastIndex

        // 유사도별 그룹
        val diffResult = mutableListOf<Image>()

        images.forEachIndexed { index, image ->
            if (index == 0) {
                diffResult.add(image)
            }

            val targetPosition = index + 1

            if (targetPosition > latestIndex) {
                result.add(date to diffResult.toList())
                diffResult.clear()
                return@forEachIndexed
            }

            val target = images[targetPosition]
            val isMatched = compareTo(image.absolutePath, target.absolutePath)

            if (isMatched) {
                diffResult.add(target)
            } else {
                result.add(date to diffResult.toList())
                diffResult.clear()
            }
        }

        return result
    }


    /**
     *  @param origin 선택한 이미지
     *  @param target 비교대상 이미지
     *  @return 두개의 이미지가 같은 이미지인지를 나타내는 Boolean 값
     *
     * */
    private suspend fun compareTo(origin: String, target: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Load images to compare
            val img1: Mat = Imgcodecs.imread(origin, Imgcodecs.IMREAD_COLOR)
            val img2: Mat = Imgcodecs.imread(target, Imgcodecs.IMREAD_COLOR)
            val hsvImg1 = Mat()
            val hsvImg2 = Mat()

            // Convert to HSV
            Imgproc.cvtColor(img1, hsvImg1, Imgproc.COLOR_BGR2HSV)
            Imgproc.cvtColor(img2, hsvImg2, Imgproc.COLOR_BGR2HSV)

            // Set configuration for calchist()
            val listImg1: MutableList<Mat> = mutableListOf()
            val listImg2: MutableList<Mat> = mutableListOf()

            listImg1.add(hsvImg1)
            listImg2.add(hsvImg2)

            val ranges = MatOfFloat(0f, 255f)
            val histSize = MatOfInt(50)
            val channels = MatOfInt(0)

            // Histograms
            val histImg1 = Mat()
            val histImg2 = Mat()

            // Calculate the histogram for the HSV images
            Imgproc.calcHist(listImg1, channels, Mat(), histImg1, histSize, ranges)
            Imgproc.calcHist(listImg2, channels, Mat(), histImg2, histSize, ranges)
            Core.normalize(histImg1, histImg1, 0.0, 1.0, Core.NORM_MINMAX, -1, Mat())
            Core.normalize(histImg2, histImg2, 0.0, 1.0, Core.NORM_MINMAX, -1, Mat())

            // Apply the histogram comparison methods
            // 0 - correlation: the higher the metric, the more accurate the match "> 0.9"
            // 1 - chi-square: the lower the metric, the more accurate the match "< 0.1"
            // 2 - intersection: the higher the metric, the more accurate the match "> 1.5"
            // 3 - bhattacharyya: the lower the metric, the more accurate the match  "< 0.3"

            val result0: Double
            val result1: Double
            val result2: Double
            val result3: Double

            result0 = Imgproc.compareHist(histImg1, histImg2, 0)
            result1 = Imgproc.compareHist(histImg1, histImg2, 1)
            result2 = Imgproc.compareHist(histImg1, histImg2, 2)
            result3 = Imgproc.compareHist(histImg1, histImg2, 3)

            // If the count that it is satisfied with the condition is over 3, two images is same.
            // count 값이 3 이상 일 경우 2개의 이미지가 같다고 판단
            var count = 0

            if (result0 > 0.9) {
                count++
            }
            if (result1 < 0.1) {
                count++
            }
            if (result2 > 1.5) {
                count++
            }
            if (result3 < 0.3) {
                count++
            }

            Timber.i("Matched Count is $count")

            count >= 3
        }
    }

    private companion object {
        private const val LIB_NAME = "opencv_java4"
    }
}