package com.capstone.swalokal

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.swalokal.databinding.ActivityMainBinding
import com.capstone.swalokal.ui.CameraActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {

        // to camera activity
        binding.cameraOption.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        binding.galleryOption.setOnClickListener {
            val intent = Intent()
            intent.action = ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a picture")
            startActivity(chooser)
        }
    }
}