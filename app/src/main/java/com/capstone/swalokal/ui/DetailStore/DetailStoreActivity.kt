package com.capstone.swalokal.ui.DetailStore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.swalokal.R
import com.capstone.swalokal.databinding.ActivityDetailStoreBinding
import com.capstone.swalokal.databinding.ActivityMapsBinding

class DetailStoreActivity : AppCompatActivity() {
    private var _binding : ActivityDetailStoreBinding ?= null
    private val binding get() = _binding

    /*

    UNTUK INTENT KE GOOGLE MAPS

    val latitude = 37.7749 // Koordinat latitude tujuan
    val longitude = -122.4194 // Koordinat longitude tujuan
    val label = "Tujuan" // Nama atau label lokasi tujuan

    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps") // Menentukan paket aplikasi Google Maps

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        // Aplikasi Google Maps tidak terpasang, tangani secara sesuai
    }

    */

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoreBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}