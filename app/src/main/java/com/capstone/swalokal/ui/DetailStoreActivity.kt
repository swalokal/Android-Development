package com.capstone.swalokal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.swalokal.R

class DetailStoreActivity : AppCompatActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_store)
    }
}