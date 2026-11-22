package com.aline.memorylane.sensors.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import android.location.LocationListener
import android.os.Looper

object LocationHelper {
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onResult: (Location?) -> Unit
    ) {
        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

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
            onResult(null)
        }
    }
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(
        context: Context,
        onUpdate: (Location) -> Unit
    ): LocationListener? {
        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val provider = when {
            (fine || coarse) &&
                    manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER

            fine && manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER

            else -> return null
        }
        val listener = LocationListener { location ->
            onUpdate(location)
        }
        return try {
            manager.requestLocationUpdates(
                provider,
                3000L,
                5f,
                listener,
                Looper.getMainLooper()
            )
            listener
        } catch (e: SecurityException) {
            null
        }
    }
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

