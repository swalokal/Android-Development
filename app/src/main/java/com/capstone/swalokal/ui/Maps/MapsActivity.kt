package com.capstone.swalokal.ui.Maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.swalokal.R
import com.capstone.swalokal.api.response.PredictItem
import com.capstone.swalokal.databinding.ActivityMapsBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.concurrent.TimeUnit
import android.location.Location
import android.net.Uri
import com.capstone.swalokal.distanceBetween


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var bottomSheetContainer: FrameLayout

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private var userLocation: LatLng? = null

    private val boundsBuilder = LatLngBounds.Builder()

    private var previousPolyline: Polyline? = null

    private lateinit var mMap: GoogleMap
    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding

    companion object {
        private const val TAG = "MapsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        hideSystemUI()

        bottomSheetContainer = binding?.bottomSheetContainer!!
        BottomSheetBehavior.from(bottomSheetContainer).state = BottomSheetBehavior.STATE_HIDDEN

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // bottom sheet
        binding?.expand?.setOnClickListener {
            Log.d("Maps", "Expand")
            toggleBottomSheet()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient((this))

        // bottom sheet state
        if (savedInstanceState != null) {
            val sheetState =
                savedInstanceState.getInt("bottom_sheet_state", BottomSheetBehavior.STATE_COLLAPSED)
            bottomSheetBehavior.state = sheetState
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("bottom_sheet_state", bottomSheetBehavior.state)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLastLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this@MapsActivity, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }

    private val resolutionLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> Log.i(TAG, "onActivityResult: All location settings are satisfied.")
            RESULT_CANCELED ->
                Toast.makeText(
                    this@MapsActivity,
                    "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun toggleBottomSheet() {

        bottomSheetContainer = binding?.bottomSheetContainer!!
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.peekHeight = 200

        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            // Jika Bottom Sheet sedang terbuka, tutup
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding?.expand?.setImageResource(R.drawable.ic_expand_24)
        } else {
            // Jika Bottom Sheet sedang tertutup, buka
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding?.expand?.setImageResource(R.drawable.ic_collapsed_36)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        createLocationRequest()

        mMap.setOnMarkerClickListener(this)

        // items
        val predictItems = intent.getParcelableArrayListExtra<PredictItem>("predictItems")
        Log.d("PredictItems", predictItems.toString())

        predictItems?.let {
            if (it.isNotEmpty()) {
                // show mark
                it.forEach { predictItem ->
                    predictItem?.let { item ->
                        binding?.productName?.text = "\"${item.name}\""
                        val loc =
                            LatLng(predictItem.latitude as Double, predictItem.longtitude as Double)
                        val marker = mMap.addMarker(
                            MarkerOptions().position(loc)
                                .title("Toko ${predictItem.toko}")
                        )

                        marker?.tag = predictItem

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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        when {
            permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                // Precise location access granted.
                getMyLastLocation()
            }
            permission[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // Only approximate location access granted.
                getMyLastLocation()
            }
            else -> {
                // No location access granted.
                Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUserMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title("Lokasi Saya")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))

    }

    private fun checkPermission(permisssion: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permisssion
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)
                    showUserMarker(location)
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMarkerClick(marker: Marker): Boolean {
        previousPolyline?.remove()

        val predictItem = marker.tag as PredictItem

        // lokasi pengguna
        if (userLocation != null) {

//             lokasi mark
            val markerLocation = marker.position

            Log.d("User location", userLocation.toString())
            Log.d("Target location", markerLocation.toString())

            val originLocationRoute = userLocation.toString().substringAfter("(").substringBefore(")")
            val destinationLocationRoute = markerLocation.toString().substringAfter("(").substringBefore(")")

            Log.d("User location formatted", originLocationRoute)
            Log.d("Target location formatt", destinationLocationRoute)

            // menampilkan info loc
            toggleBottomSheet()

            val title = marker.title
            val distance = distanceBetween(
                userLocation!!.latitude, userLocation!!.longitude,
                markerLocation.latitude, markerLocation.longitude
            )
            val simpleFormatDistance = String.format("%.2f", distance)
            val message = "Toko $title"

//            Toast.makeText(this@MapsActivity, message, Toast.LENGTH_SHORT).show()

            Log.d("maps", "jarak $simpleFormatDistance")


            // Tampilkan informasi toko
            binding?.namaToko?.text = "Toko ${predictItem.toko}"
            binding?.let {
                Glide.with(this)
                    .load(predictItem.photoUrl)
                    .error(R.drawable.ic_gallery_27)
                    .centerCrop()
                    .into(it.previewImageView)
            }
            binding?.jarakToko?.text = "$simpleFormatDistance m"

            binding?.rute?.setOnClickListener {
                val intentUri = "https://www.google.com/maps/dir/?api=1&origin=$originLocationRoute&destination=$destinationLocationRoute"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri))
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }

        } else {
            Toast.makeText(
                this@MapsActivity,
                "Lokasi anda tidak tersedia",
                Toast.LENGTH_SHORT
            ).show()
        }

        return true
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