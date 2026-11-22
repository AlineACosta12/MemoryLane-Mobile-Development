package com.aline.memorylane.sensors.orientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


// Class that reads the phone's rotation sensor to find which way it is facing
class OrientationSensor(
    context: Context,
    private val onHeadingChanged: (Float) -> Unit
) : SensorEventListener {

    // Android's sensor service
    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Rotation vector sensor (combines compass, accelerometer and gyroscope)
    // Can be null if the phone doesn't have it
    private val rotationSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    // Arrays to store the results of the calculations
    private val rotationMatrix = FloatArray(9)      // 3x3 matrix
    private val orientationValues = FloatArray(3)   // azimuth, pitch, roll

    // Start listening to the orientation sensor
    // Returns false if the sensor doesn't exist on this phone
    fun start(): Boolean {
        val sensor = rotationSensor ?: return false

        // SENSOR_DELAY_UI is a speed that's good enough for updating the screen
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

        // Turn the sensor values into a rotation matrix
        SensorManager.getRotationMatrixFromVector(
            rotationMatrix,
            event.values
        )

        // Get the orientation angles from the matrix
        SensorManager.getOrientation(
            rotationMatrix,
            orientationValues
        )

        val azimuth = Math.toDegrees(
            orientationValues[0].toDouble()
        )

        val heading = ((azimuth + 360.0) % 360.0).toFloat()

        // Send the new heading back to the activity
        onHeadingChanged(heading)
    }

    // Has to be here because of SensorEventListener
    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {
        // No action needed for now
    }
}
