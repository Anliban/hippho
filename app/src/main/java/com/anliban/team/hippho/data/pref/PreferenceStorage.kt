package com.anliban.team.hippho.data.pref

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var deletedCount: Long
    var deletedFileSize: Long
}

class SharedPreferenceStorage(context: Context) : PreferenceStorage {

    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, MODE_PRIVATE
        )
    }

    override var deletedCount: Long by LongPreference(prefs, PREF_DELETED_COUNT, 0)

    override var deletedFileSize: Long by LongPreference(prefs, PREF_DELETED_FILE_SIZE, 0)

    companion object {
        const val PREFS_NAME = "hippho"
        const val PREF_DELETED_COUNT = "pref_deleted_count"
        const val PREF_DELETED_FILE_SIZE = "pref_deleted_file_size"
    }
}

class LongPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Long
) : ReadWriteProperty<Any, Long> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return preferences.value.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        preferences.value.edit { putLong(name, value) }
    }
}
