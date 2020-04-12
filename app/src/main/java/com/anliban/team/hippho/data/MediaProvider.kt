package com.anliban.team.hippho.data

import android.app.RecoverableSecurityException
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.model.Result
import com.anliban.team.hippho.util.dateToTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

interface MediaProvider {
    fun getImages(option: ImageQueryOption): Flow<List<Image>>
    fun getImages(option: ImageQueryOption, ids: List<Long>?): Flow<List<Image>>
    fun delete(ids: List<Long>): Flow<Result<Unit>>
}

class MediaProviderImpl(context: Context) : MediaProvider {

    private val contentResolver by lazy { context.contentResolver }

    private val defaultSelectionArgs by lazy {
        arrayOf(
            dateToTimestamp(day = 1, month = 1, year = 1970).toString()
        )
    }

    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        DATE_COLUMN
    )

    private val sortOrder = "$DATE_COLUMN DESC"

    override fun getImages(option: ImageQueryOption): Flow<List<Image>> {
        val cursor = contentResolver.query(
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
        val cursor = contentResolver.query(
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

    override fun delete(ids: List<Long>): Flow<Result<Unit>> {
        return flow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    remove(ids)
                    emit(Result.Success(Unit))
                } catch (e: RecoverableSecurityException) {
                    emit(Result.Error(e))
                }
            } else {
                remove(ids)
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun remove(ids: List<Long>) {
        withContext(Dispatchers.IO) {
            contentResolver.delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                getQuerySelection(ImageQueryOption.ID) + getIdsSelectionOption(ids),
                getQuerySelectionArgs(getStringIds(ids))
            )
        }
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
        val dateColumn = getColumnIndexOrThrow(Companion.DATE_COLUMN)

        val id = getLong(idColumn)
        val displayName = getString(displayNameColumn)
        val contentUri = Uri.withAppendedPath(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            id.toString()
        )
        val dateTaken =
            if (Companion.DATE_COLUMN == MediaStore.Images.Media.DATE_TAKEN) {
                Date(getLong(dateColumn))
            } else {
                Date(getLong(dateColumn) * 1000)
            }

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
            ImageQueryOption.DATE -> "$DATE_COLUMN >= ?"
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

    companion object {
        val DATE_COLUMN =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.DATE_TAKEN
            } else {
                MediaStore.Images.Media.DATE_ADDED
            }
    }
}

enum class ImageQueryOption {
    DATE, ID
}
