package com.capstone.swalokal.ui.Maps

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.swalokal.R
import com.capstone.swalokal.api.response.PredictItem
import com.capstone.swalokal.databinding.ActivityMapsBinding
import com.capstone.swalokal.ui.Search.MapsListStoreAdapter
import com.capstone.swalokal.ui.Search.SearchAdapter
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var bottomSheetContainer: FrameLayout

    private lateinit var adapter: MapsListStoreAdapter

    private val boundsBuilder = LatLngBounds.Builder()


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        setupRv()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // bottom sheet
        binding.expand.setOnClickListener {
            Log.d("Maps", "Expand")
            toggleBottomSheet()
        }
    }


    private fun toggleBottomSheet() {

        bottomSheetContainer = binding.bottomSheetContainer
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.peekHeight = 200

        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            // Jika Bottom Sheet sedang terbuka, tutup
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.expand.setImageResource(R.drawable.ic_expand_24)
        } else {
            // Jika Bottom Sheet sedang tertutup, buka
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.expand.setImageResource(R.drawable.ic_collapsed_36)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        // dummy
        val predictItems = intent.getParcelableArrayListExtra<PredictItem>("predictItems")
        Log.d("PredictItems", predictItems.toString())


        predictItems?.let {
            if (it.isNotEmpty()) {
                // submit data to adapter
                adapter.submitList(predictItems)

                // show mark
                it.forEach { predictItem ->
                        predictItem?.let { item ->
                            binding.productName.text = "\"${item.name}\""
                        val loc =
                            LatLng(predictItem.latitude as Double, predictItem.longtitude as Double)
                        mMap.addMarker(
                            MarkerOptions().position(loc)
                                .title("Toko ${predictItem.toko}")
                        )
                        boundsBuilder.include(loc)

                    }
                }
                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
            }
        }

    }

    private fun setupRv() {

        adapter = MapsListStoreAdapter()
        binding.rvItemStore.adapter = adapter
        // rv
        binding.rvItemStore.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)

        binding.rvItemStore.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding.rvItemStore.addItemDecoration(itemDecoration)
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