package com.anliban.team.hippho.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.anliban.team.hippho.model.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
    override fun getImages(): Flow<List<Image>> {
        return flow {
            val fileList = mutableListOf<Image>()
            val projection = arrayOf(MediaStore.Files.FileColumns._ID)
            val sortOrder = MediaStore.Images.Media._ID + " DESC"

            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            cursor?.use {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                    fileList.add(Image(contentUri.toString()))
                }
            }

            emit(fileList)
        }
    }

}