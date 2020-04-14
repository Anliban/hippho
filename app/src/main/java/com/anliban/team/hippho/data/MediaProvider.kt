package com.anliban.team.hippho.data

import android.content.ContentProviderOperation
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.AUTHORITY
import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.util.bytesToMegaBytes
import com.anliban.team.hippho.util.dateToTimestamp
import com.anliban.team.hippho.util.roundTo2Decimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

interface MediaProvider {
    fun getImages(option: ImageQueryOption): Flow<List<Image>>
    fun getImages(option: ImageQueryOption, ids: List<Long>?): Flow<List<Image>>
    suspend fun delete(image: List<Image>)
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
        DATE_COLUMN,
        MediaStore.Images.Media.SIZE
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

    override suspend fun delete(image: List<Image>) {
        withContext(Dispatchers.IO) {
            val operations = arrayListOf<ContentProviderOperation>()

            image.forEach {
                val providerOperation = ContentProviderOperation.newDelete(Uri.parse(it.contentUri))
                    .withSelection("${MediaStore.Images.Media._ID} = ?", arrayOf(it.id.toString()))
                    .build()
                operations.add(providerOperation)
            }

            contentResolver.applyBatch(AUTHORITY, operations)
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
        val dateColumn = getColumnIndexOrThrow(DATE_COLUMN)
        val fileSizeColumn = getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

        val id = getLong(idColumn)
        val displayName = getString(displayNameColumn)
        val contentUri = Uri.withAppendedPath(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            id.toString()
        )
        val dateTaken =
            if (DATE_COLUMN == MediaStore.Images.Media.DATE_TAKEN) {
                Date(getLong(dateColumn))
            } else {
                Date(getLong(dateColumn) * 1000)
            }
        val fileSize = bytesToMegaBytes(getString(fileSizeColumn)).roundTo2Decimal()

        Timber.i("Name : $displayName / Date : $dateTaken / ID : $id / path : $contentUri / size : $fileSize")

        return Image(
            id = id,
            fileName = displayName,
            date = dateTaken,
            contentUri = contentUri.toString(),
            fileSize = fileSize
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
