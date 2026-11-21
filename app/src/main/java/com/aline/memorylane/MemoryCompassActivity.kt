package com.aline.memorylane

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.aline.memorylane.sensors.location.LocationHelper
import com.aline.memorylane.sensors.orientation.OrientationSensor
import com.aline.memorylane.sensors.compass.CompassCalculator
import com.aline.memorylane.sensors.compass.CompassResult
import com.aline.memorylane.ui.screens.MemoryCompassScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme

class MemoryCompassActivity : AppCompatActivity() {
    private lateinit var orientationSensor: OrientationSensor
    private var heading by mutableStateOf<Float?>(null)
    private var currentLocation by mutableStateOf<Location?>(null)
    private var compassResult by mutableStateOf<CompassResult?>(null)
    private var selectedMemory: Memory? = null
    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted = permissions[
                Manifest.permission.ACCESS_FINE_LOCATION
            ] == true || permissions[
                Manifest.permission.ACCESS_COARSE_LOCATION
            ] == true

            if (granted) {
                readCurrentLocation()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val memoryId = intent.getIntExtra(
            "memory_id",
            intent.getIntExtra("memoryId", -1)
        )
        selectedMemory = SampleMemories.memories.find {
            it.id == memoryId
        }
        orientationSensor = OrientationSensor(this) { angle ->
            heading = angle
            updateCompass()
        }
        setContent {
            MemoryLaneTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .systemBarsPadding()
                ) {
                    MemoryCompassScreen(
                        memory = selectedMemory,
                        onBack = { finish() },
                        heading = heading,
                        compassResult = compassResult
                    )
                }
            }
        }
        if (selectedMemory?.latitude != null &&
            selectedMemory?.longitude != null
        ) {
            requestCurrentLocation()
        }
    }
    private fun requestCurrentLocation() {
        val fineGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            readCurrentLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
    private fun readCurrentLocation() {
        LocationHelper.getCurrentLocation(this) { location ->
            currentLocation = location
            if (location != null) {
                Toast.makeText(
                    this@MemoryCompassActivity,
                    "GPS location received",
                    Toast.LENGTH_SHORT
                ).show()
                updateCompass()
            } else {
                Toast.makeText(
                    this,
                    "Could not get current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun updateCompass() {
        val location = currentLocation ?: return
        val angle = heading ?: return
        val memory = selectedMemory ?: return
        val latitude = memory.latitude ?: return
        val longitude = memory.longitude ?: return

        compassResult = CompassCalculator.calculate(
            currentLocation = location,
            targetLatitude = latitude,
            targetLongitude = longitude,
            magneticHeading = angle
        )
    }
    override fun onResume() {
        super.onResume()

        if (!orientationSensor.start()) {
            Toast.makeText(
                this,
                "Orientation sensor not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun onPause() {
        orientationSensor.stop()
        super.onPause()
    }
}
