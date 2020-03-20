package com.anliban.team.hippho.ui.main

import android.Manifest
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.anliban.team.hippho.R
import com.anliban.team.hippho.base.BaseActivity
import com.anliban.team.hippho.databinding.ActivityMainBinding
import com.anliban.team.hippho.util.compareHistogram
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPermission()
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        setupBinding()

    }

    private fun getImages() {
        val result: ArrayList<String> = ArrayList()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaColumns.DATA, MediaColumns.DISPLAY_NAME)

        val cursor: Cursor? = contentResolver.query(
            uri,
            projection,
            null,
            null,
            MediaColumns.DATE_ADDED + " desc"
        )
        cursor?.let{

            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaColumns.DATA)
            val columnDisplayName: Int = cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME)
            var lastIndex: Int
            while (cursor.moveToNext()) {
                val absolutePathOfImage: String = cursor.getString(columnIndex)
                val nameOfFile: String = cursor.getString(columnDisplayName)
                lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile)
                lastIndex = if (lastIndex >= 0) lastIndex else nameOfFile.length - 1
                if (!TextUtils.isEmpty(absolutePathOfImage)) {
                    result.add(absolutePathOfImage)
                }
            }
        }

        for (string in result) {
            Log.i("${this.localClassName} | getImages", "|$string|")
        }
        val image1 = result[0]
        val image2= result[1]
        Log.d("@@@compare","::: ${compareHistogram(applicationContext,image1,image2)}")


        cursor?.close()

    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@MainActivity
    }

    private fun setPermission() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    getImages()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    finish()
                }

            })
            .setRationaleMessage(resources.getString(R.string.permission_string2))
            .setDeniedMessage(resources.getString(R.string.permission_string1))
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check()
    }
}
