package com.capstone.swalokal.ui

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.capstone.swalokal.R
import com.capstone.swalokal.databinding.ActivityCameraBinding
import com.capstone.swalokal.databinding.ActivityProductImageBinding
import com.capstone.swalokal.rotateFile
import java.io.File

class ProductImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductImageBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()

        val myFile = intent?.getSerializableExtra("picture") as? File
        val isBackCamera = intent?.getBooleanExtra("isBackCamera", true) as Boolean
        Log.d("Product Image", "isBackCamera : $isBackCamera")


        myFile?.let {
            rotateFile(it, isBackCamera)
            getFile = it
            binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(it.path))
        }

    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}