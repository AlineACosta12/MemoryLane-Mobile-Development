package com.aline.memorylane.sensors.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat

// Helper object for everything to do with getting the user's location
object LocationHelper {
    // Gets the user's current location one time
    // The result is sent back through onResult (null if it couldn't get one)
    // @SuppressLint because we check the permissions ourselves below
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onResult: (Location?) -> Unit
    ) {
        // Android's location service
        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        // Check precise location permission
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Check approximate location permission
        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Pick which provider to use
        // Network (wifi/mobile) is tried first because it's usually faster,
        // then GPS. If neither works, return null and stop.
        val provider = when {
            coarseGranted &&
                    manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER

            fineGranted &&
                    manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER

            else -> {
                onResult(null)
                return
            }
        }
        // Ask for the current location
        // The result comes back on the main thread so we can update the UI
        try {
            LocationManagerCompat.getCurrentLocation(
                manager,
                provider,
                android.os.CancellationSignal(),
                ContextCompat.getMainExecutor(context)
            ) { location ->
                onResult(location)
            }
        } catch (e: SecurityException) {
            // Permission was removed - just return null instead of crashing
            onResult(null)
        }
    }

    // Starts listening for location updates while the user moves
    // Returns the listener so we can stop it later (or null if it couldn't start)
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(
        context: Context,
        onUpdate: (Location) -> Unit
    ): LocationListener? {
        // Android's location service
        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
        // Check precise location permission
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        // Check approximate location permission
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        // Pick a provider - network first, then GPS
        // If neither is available, return null
        val provider = when {
            (fine || coarse) &&
                    manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER

            fine && manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER

            else -> return null
        }
        // Listener that gets called every time there is a new location
        val listener = LocationListener { location ->
            onUpdate(location)
        }
        return try {
            // Get updates every 3 seconds or when the user moves 5 metres
            manager.requestLocationUpdates(
                provider,
                3000L,
                5f,
                listener,
                Looper.getMainLooper()
            )
            listener
        } catch (e: SecurityException) {
            // Permission problem - return null instead of crashing
            null
        }
    }

    // Stops the location updates (used in onPause to save battery)
    fun stopLocationUpdates(
        context: Context,
        listener: LocationListener
    ) {
        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
        manager.removeUpdates(listener)
    }
}
