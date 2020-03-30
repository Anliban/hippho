package com.anliban.team.hippho.util

import android.content.Context
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfFloat
import org.opencv.core.MatOfInt
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

fun compareHistogram(context: Context, filename1: String?, filename2: String?): Int {
    var retVal = 0
    val startTime = System.currentTimeMillis()
    val appPath: String = context.applicationContext.filesDir.absolutePath
    System.loadLibrary("opencv_java4")
    // Load images to compare
    val img1: Mat = Imgcodecs.imread(filename1, Imgcodecs.IMREAD_COLOR)
    val img2: Mat = Imgcodecs.imread(filename2, Imgcodecs.IMREAD_COLOR)
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
    // Calculate the histogram for the HSV imgaes
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
    println("Method [0] $result0")
    println("Method [1] $result1")
    println("Method [2] $result2")
    println("Method [3] $result3")
    // If the count that it is satisfied with the condition is over 3, two images is same.
    var count = 0
    if (result0 > 0.9) count++
    if (result1 < 0.1) count++
    if (result2 > 1.5) count++
    if (result3 < 0.3) count++
    if (count >= 3) retVal = 1
    val estimatedTime = System.currentTimeMillis() - startTime
    println("estimatedTime=" + estimatedTime + "ms")
    return retVal
}