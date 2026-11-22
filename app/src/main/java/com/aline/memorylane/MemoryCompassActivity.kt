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
import com.aline.memorylane.sensors.compass.CompassCalculator
import com.aline.memorylane.sensors.compass.CompassResult
import com.aline.memorylane.sensors.location.LocationHelper
import com.aline.memorylane.sensors.orientation.OrientationSensor
import com.aline.memorylane.ui.screens.MemoryCompassScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme

// Activity for the Memory Compass
// Uses GPS and the orientation sensor to point the user towards a saved memory
class MemoryCompassActivity : AppCompatActivity() {
    // Sensor that tells us which way the phone is facing
    private lateinit var orientationSensor: OrientationSensor
    // These use mutableStateOf so the screen updates when they change
    private var heading by mutableStateOf<Float?>(null)           // direction the phone is facing
    private var currentLocation by mutableStateOf<Location?>(null) // user's GPS location
    private var compassResult by mutableStateOf<CompassResult?>(null) // bearing + distance to the memory
    // The memory we are pointing to
    private var selectedMemory: Memory? = null
    // Asks the user for location permission
    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            // True if precise or approximate location was allowed
            val granted = permissions[
                Manifest.permission.ACCESS_FINE_LOCATION
            ] == true || permissions[
                Manifest.permission.ACCESS_COARSE_LOCATION
            ] == true

            if (granted) {
                readCurrentLocation()
            } else {
                // Let the user know the compass can't work without permission
                Toast.makeText(
                    this,
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Let the app draw behind the status bar and navigation bar
        enableEdgeToEdge()
        // Get the memory id from the intent
        // (checks both "memory_id" and "memoryId" just in case, -1 if missing)
        val memoryId = intent.getIntExtra(
            "memory_id",
            intent.getIntExtra("memoryId", -1)
        )
        // Find the memory with that id
        selectedMemory = SampleMemories.memories.find {
            it.id == memoryId
        }
        // Set up the orientation sensor
        // Every time the phone turns, save the new heading and update the compass
        orientationSensor = OrientationSensor(this) { angle ->
            heading = angle
            updateCompass()
        }
        setContent {
            MemoryLaneTheme {
                // systemBarsPadding stops the content going under the system bars
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
        // Only ask for the location if the memory actually has coordinates
        if (selectedMemory?.latitude != null &&
            selectedMemory?.longitude != null
        ) {
            requestCurrentLocation()
        }
    }

    // Checks if we already have location permission, and asks for it if not
    private fun requestCurrentLocation() {
        // Check precise location permission
        val fineGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Check approximate location permission
        val coarseGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            // Permission already given - get the location
            readCurrentLocation()
        } else {
            // No permission yet - ask the user
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    // Gets the user's current GPS location once
    private fun readCurrentLocation() {
        LocationHelper.getCurrentLocation(this) { location ->
            currentLocation = location
            if (location != null) {
                Toast.makeText(
                    this@MemoryCompassActivity,
                    "GPS location received",
                    Toast.LENGTH_SHORT
                ).show()
                // We have a location now, so work out the compass direction
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

    // Works out the direction and distance to the memory
    // Only runs if we have everything we need, otherwise it just stops (return)
    private fun updateCompass() {
        val location = currentLocation ?: return
        val angle = heading ?: return
        val memory = selectedMemory ?: return
        val latitude = memory.latitude ?: return
        val longitude = memory.longitude ?: return

        // Use our calculator to get the arrow angle, bearing and distance
        compassResult = CompassCalculator.calculate(
            currentLocation = location,
            targetLatitude = latitude,
            targetLongitude = longitude,
            magneticHeading = angle
        )
    }

    // Start the orientation sensor when the screen is shown
    override fun onResume() {
        super.onResume()

        // start() returns false if the phone doesn't have the sensor
        if (!orientationSensor.start()) {
            Toast.makeText(
                this,
                "Orientation sensor not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Stop the sensor when leaving the screen to save battery
    override fun onPause() {
        orientationSensor.stop()
        super.onPause()
    }
}
