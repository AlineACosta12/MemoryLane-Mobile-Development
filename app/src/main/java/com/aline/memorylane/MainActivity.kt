package com.aline.memorylane

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.aline.memorylane.sensors.location.LocationHelper
import com.aline.memorylane.ui.screens.HomeScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme

// Main activity - the first screen the user sees (home screen)
class MainActivity : AppCompatActivity() {
    // The user's current location
    private var currentLocation by mutableStateOf<Location?>(null)
    // Listener that receives location updates
    private var locationListener: LocationListener? = null
    // Asks the user for location permission
    // If at least one permission is allowed, start getting the location
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { it }) {
            loadLocation()
        }
    }

    // Starts listening for location updates
    private fun loadLocation() {
        // Already listening - don't start a second listener
        if (locationListener != null) return
        // Update the location every time the phone gets a new one
        locationListener = LocationHelper.startLocationUpdates(this) { location ->
            currentLocation = location
        }
        // Also get the current location once, so the screen updates straight away
        LocationHelper.getCurrentLocation(this) { location ->
            if (location != null) {
                currentLocation = location
            }
        }
    }

    // Checks if we already have location permission, and asks for it if not
    private fun requestLocation() {
        // Check precise location permission
        val fine = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Check approximate location permission
        val coarse = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fine || coarse) {
            // Permission already given - get the location
            loadLocation()
        } else {
            // No permission yet - ask the user
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Let the app draw behind the status bar and navigation bar
        enableEdgeToEdge()

        setContent {
            MemoryLaneTheme {
                HomeScreen(
                    // Open the Add Memory screen
                    onAddMemory = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                AddMemoryActivity::class.java
                            )
                        )
                    },

                    // Open the All Memories screen
                    onViewAll = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                AllMemoriesActivity::class.java
                            )
                        )
                    },
                    // Open the detail screen for the memory that was tapped
                    onOpenMemory = { memoryId ->
                        val intent = Intent(
                            this@MainActivity,
                            MemoryDetailActivity::class.java
                        )
                        // Send the memory id so the detail screen knows which one to show
                        intent.putExtra("memory_id", memoryId)
                        startActivity(intent)
                    },
                    // Pass the location so the home screen can show nearby memories
                    currentLocation = currentLocation
                )
            }
        }
    }

    // Runs every time the screen comes back into view
    // Start getting the location again
    override fun onResume() {
        super.onResume()
        requestLocation()
    }

    // Runs when the user leaves the screen
    // Stop location updates to save battery
    override fun onPause() {
        locationListener?.let {
            LocationHelper.stopLocationUpdates(this, it)
        }
        locationListener = null
        super.onPause()
    }
}
