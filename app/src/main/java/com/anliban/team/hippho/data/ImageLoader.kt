package com.anliban.team.hippho.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.util.dateToTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

interface ImageLoader {
    fun getImages(option: ImageQueryOption): Flow<List<Image>>
    fun getImages(option: ImageQueryOption, ids: List<Long>?): Flow<List<Image>>
}

@RequiresApi(Build.VERSION_CODES.Q)
class ImageLoaderImpl(private val context: Context) : ImageLoader {

    private val defaultSelectionArgs by lazy {
        arrayOf(
            dateToTimestamp(day = 1, month = 1, year = 1970).toString()
        )
    }

    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_TAKEN
    )

    private val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

    override fun getImages(option: ImageQueryOption): Flow<List<Image>> {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            getQuerySelection(option),
            getQuerySelectionArgs(),
            sortOrder
        )

        return flow {
            val images = cursor.search()
            emit(images)
        }
            .onCompletion { cursor?.close() }
            .flowOn(Dispatchers.IO)
    }

    override fun getImages(option: ImageQueryOption, ids: List<Long>?): Flow<List<Image>> {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            getQuerySelection(option) + getIdsSelectionOption(ids),
            getQuerySelectionArgs(getStringIds(ids)),
            sortOrder
        )

        return flow {
            val images = cursor.search()
            emit(images)
        }
            .onCompletion { cursor?.close() }
            .flowOn(Dispatchers.IO)
    }

    private suspend fun Cursor?.search(): List<Image> {
        return withContext(Dispatchers.IO) {
            val images = mutableListOf<Image>()

            this@search?.use {
                while (it.moveToNext()) {
                    val image = it.getImage()
                    images.add(image)
                }
            }
            images
        }
    }

    private fun Cursor.getImage(): Image {
        val idColumn = getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val displayNameColumn = getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val dateTakenColumn = getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

        val id = getLong(idColumn)
        val dateTaken = Date(getLong(dateTakenColumn))
        val displayName = getString(displayNameColumn)
        val contentUri = Uri.withAppendedPath(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            id.toString()
        )

        Timber.i("Name : $displayName / Date : $dateTaken / ID : $id / path : $contentUri ")

        return Image(
            id = id,
            fileName = displayName,
            date = dateTaken,
            contentUri = contentUri.toString()
        )
    }

    private fun getQuerySelection(queryOption: ImageQueryOption): String {
        return when (queryOption) {
            ImageQueryOption.DATE -> "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
            ImageQueryOption.ID -> "${MediaStore.Images.Media._ID} IN "
        }
    }

    private fun getIdsSelectionOption(ids: List<Long>?): String {
        ids ?: return ""

        var result = "("
        ids.forEachIndexed { position, _ ->
            result += "?"

            if (position != ids.lastIndex) {
                result += ","
            }
            if (position == ids.lastIndex) {
                result += ")"
            }
        }

        return result
    }

    private fun getQuerySelectionArgs(args: Array<String>? = null): Array<String> {
        return args ?: defaultSelectionArgs
    }

    private fun getStringIds(ids: List<Long>?): Array<String>? {
        return ids?.let {
            ids.map { it.toString() }.toTypedArray()
        }
    }
}

enum class ImageQueryOption {
    DATE, ID
}
