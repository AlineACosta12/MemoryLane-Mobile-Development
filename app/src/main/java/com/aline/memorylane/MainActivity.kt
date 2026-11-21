package com.aline.memorylane

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.aline.memorylane.ui.screens.HomeScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.aline.memorylane.sensors.location.LocationHelper

class MainActivity : AppCompatActivity() {
    private var currentLocation by mutableStateOf<Location?>(null)
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { it }) {
            loadLocation()
        }
    }
    private fun loadLocation() {
        LocationHelper.getCurrentLocation(this) { location ->
            currentLocation = location
        }
    }
    private fun requestLocation() {
        val fine = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fine || coarse) {
            loadLocation()
        } else {
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
        enableEdgeToEdge()

        setContent {
            MemoryLaneTheme {
                HomeScreen(
                    onAddMemory = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                AddMemoryActivity::class.java
                            )
                        )
                    },

                    onViewAll = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                AllMemoriesActivity::class.java
                            )
                        )
                    },
                    onOpenMemory = { memoryId ->
                        val intent = Intent(
                            this@MainActivity,
                            MemoryDetailActivity::class.java
                        )
                        intent.putExtra("memory_id", memoryId)
                        startActivity(intent)
                    },
                    currentLocation = currentLocation
                )
            }
            requestLocation()
        }
    }
}
