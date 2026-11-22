package com.aline.memorylane.sensors.compass

import android.hardware.GeomagneticField
import android.location.Location

data class CompassResult(
    val distanceMeters: Float,
    val bearingDegrees: Float,
    val arrowRotationDegrees: Float
)

object CompassCalculator {
    fun calculate(
        currentLocation: Location,
        targetLatitude: Double,
        targetLongitude: Double,
        magneticHeading: Float
    ): CompassResult {
        // Create the destination using the memory coordinates
        val destination = Location("memory").apply {
            latitude = targetLatitude
            longitude = targetLongitude
        }

        // Calculate distance in metres
        val distance = currentLocation.distanceTo(destination)

        // Calculate bearing relative to true north
        val bearing = (
                currentLocation.bearingTo(destination) + 360f
                ) % 360f
        // Convert magnetic north to true north
        val geomagneticField = GeomagneticField(
            currentLocation.latitude.toFloat(),
            currentLocation.longitude.toFloat(),
            currentLocation.altitude.toFloat(),
            System.currentTimeMillis()
        )
        val trueHeading = (
                magneticHeading + geomagneticField.declination + 360f
                ) % 360f
        // Calculate arrow rotation relative to phone orientation
        val arrowRotation = (
                bearing - trueHeading + 360f
                ) % 360f
        return CompassResult(
            distanceMeters = distance,
            bearingDegrees = bearing,
            arrowRotationDegrees = arrowRotation
        )
    }
}
