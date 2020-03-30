package com.anliban.team.hippho.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.anliban.team.hippho.model.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

interface ImageLoader {
    fun getImages(): Flow<List<Image>>
}

class ImageLoaderImpl(private val context: Context) : ImageLoader {

    /*
    * for (string in result) {
            Log.i("${this.localClassName} | getImages", "|$string|")
        }
        val image1 = result[0]
        val image2 = result[1]
        Log.d("@@@compare", "::: ${compareHistogram(applicationContext, image1, image2)}")
    *
    * */
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun getImages(): Flow<List<Image>> {
        return flow {
            val fileList = mutableListOf<Image>()
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

            val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
            val selectionArgs = arrayOf(
                dateToTimestamp(day = 1, month = 1, year = 1970).toString()
            )

            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn =
                    it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val dateTaken = Date(cursor.getLong(dateTakenColumn))
                    val displayName = cursor.getString(displayNameColumn)
                    val contentUri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                    Timber.i("Name : $displayName / Date : $dateTaken / ID : $id")
                    val image = Image(
                        id = id,
                        path = contentUri.toString(),
                        date = dateTaken,
                        name = displayName
                    )
                    fileList.add(image)
                }
            }

            emit(fileList)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            formatter.parse("$day.$month.$year")?.time ?: 0
        }

}