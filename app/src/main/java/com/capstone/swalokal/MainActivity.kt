package com.capstone.swalokal

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import com.capstone.swalokal.databinding.ActivityMainBinding
import com.capstone.swalokal.ui.CameraActivity
import com.capstone.swalokal.ui.ProductImageActivity
import com.capstone.swalokal.ui.SearchActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryOption.setOnClickListener { startGallery() }
        binding.cameraOption.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
        binding.searchProduct.setOnClickListener { searchActivity() }
    }

    private fun searchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectemImg = result?.data?.data as Uri
            selectemImg.let { uri ->
                val myFile = uriToFile(uri, this@MainActivity)
                getFile = myFile

                val intent = Intent(this, ProductImageActivity::class.java)
                intent.putExtra("picture", getFile)
                startActivity(intent)

            }
        }
    }
}