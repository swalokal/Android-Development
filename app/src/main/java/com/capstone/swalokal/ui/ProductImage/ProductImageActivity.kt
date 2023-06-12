package com.capstone.swalokal.ui.ProductImage

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.capstone.swalokal.ViewModelFactory
import com.capstone.swalokal.databinding.ActivityProductImageBinding
import com.capstone.swalokal.reduceFileImage
import com.capstone.swalokal.rotateFile
import com.capstone.swalokal.ui.MapsActivity
import java.io.File
import com.capstone.swalokal.api.Result
import com.capstone.swalokal.ui.CameraActivity
import com.example.storyapp.di.Injection

class ProductImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductImageBinding
    private var getFile: File? = null
    private lateinit var productImageViewModel: ProductImageViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        setupViewModel()

        val myFile = intent?.getSerializableExtra("picture") as? File
        val isBackCamera = intent?.getBooleanExtra("isBackCamera", true) as Boolean
        Log.d("Product Image", "isBackCamera : $isBackCamera")


        myFile?.let {
            rotateFile(it, isBackCamera)
            getFile = it
            binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(it.path))
        }

        // testing ke halaman maps activity
        binding.findButton.setOnClickListener {
            uploadImage()
        }

    }

    private fun setupViewModel() {
        val repository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(repository)
        productImageViewModel = ViewModelProvider(this, viewModelFactory)[ProductImageViewModel::class.java]

    }

    // upload and make predict
    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            productImageViewModel.uploadPhoto(file)

            // Upload Event
            productImageViewModel.uploadResult.observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(
                            this@ProductImageActivity,
                            "Success upload photo",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("productImage", result.data.toString())

//                        val intent = Intent(this, MapsActivity::class.java)
//                        startActivity(intent)
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            this@ProductImageActivity,
                            "Fail upload photo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        } else {
            Toast.makeText(
                this@ProductImageActivity,
                "Image cannot be empty",
                Toast.LENGTH_SHORT
            ).show()
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