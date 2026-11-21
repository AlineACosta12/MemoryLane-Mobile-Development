package com.aline.memorylane.sensors.orientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class OrientationSensor(
    context: Context,
    private val onHeadingChanged: (Float) -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val rotationSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val rotationMatrix = FloatArray(9)
    private val orientationValues = FloatArray(3)

    // Start listening to the orientation sensor
    fun start(): Boolean {
        val sensor = rotationSensor ?: return false

        return sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    // Stop listening to save battery
    fun stop() {
        sensorManager.unregisterListener(this)
    }

    // Called whenever the device orientation changes
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ROTATION_VECTOR) {
            return
        }

        SensorManager.getRotationMatrixFromVector(
            rotationMatrix,
            event.values
        )

        SensorManager.getOrientation(
            rotationMatrix,
            orientationValues
        )

        val azimuth = Math.toDegrees(
            orientationValues[0].toDouble()
        )

        val heading = ((azimuth + 360.0) % 360.0).toFloat()

        onHeadingChanged(heading)
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {
        // No action needed for now
    }
}
