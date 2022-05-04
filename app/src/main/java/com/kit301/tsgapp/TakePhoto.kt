package com.kit301.tsgapp

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.google.firebase.ktx.Firebase
import com.kit301.tsgapp.databinding.TakePhotoBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TakePhoto : AppCompatActivity() {

    private lateinit var ui : TakePhotoBinding

    //step 5
    private val getPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result : Boolean ->
        if (result) {
            // Permission is granted.
            takeAPicture()
        } else {
            Toast.makeText(this, "Cannot access camera, permission denied", Toast.LENGTH_LONG).show()
        }
    }

    //step 6, part 2
    private val getCameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean  ->
        //step 7, part 1
        if (result)
        {
            setPic(ui.myImageView)
            ui.btnConfirm.isVisible = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = TakePhotoBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui.btnConfirm.setOnClickListener {
            // toDO: upload image to db
            finish()
        }

        ui.btnCamera.setOnClickListener {
            requestToTakeAPicture()
        }
    }

    //step 4
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestToTakeAPicture()
    {
        getPermissionResult.launch(Manifest.permission.CAMERA)
    }

    //step 6, part 1
    private fun takeAPicture() {

        //try {
        val photoFile: File = createImageFile()!!
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.kit301.tsgapp",
            photoFile
        )
        getCameraResult.launch(photoURI)
        //} catch (e: Exception) {}

    }

    //step 6, part 3
    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    //step 7, part 2
    private fun setPic(imageView: ImageView) {
        // Get the dimensions of the View
        val targetW: Int = imageView.measuredWidth
        val targetH: Int = imageView.measuredHeight

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }
}