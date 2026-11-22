package com.aline.memorylane.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aline.memorylane.Memory
import com.aline.memorylane.R
import com.aline.memorylane.SampleMemories
import com.aline.memorylane.sensors.compass.CompassResult
import com.aline.memorylane.ui.components.CompassDial
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import java.util.Locale

// Compass screen that points the user back to a saved memory
// Uses the phone's heading and the compass result (bearing + distance) to the memory
@Composable
fun MemoryCompassScreen(
    memory: Memory?,
    onBack: () -> Unit,
    heading: Float? = null,
    compassResult: CompassResult? = null
) {
    // Work out how much to turn the compass dial so "N" keeps pointing north
    // If we have a compass result, use it; otherwise just use the phone heading
    val dialRotation = if (compassResult != null) {
        compassResult.arrowRotationDegrees -
                compassResult.bearingDegrees
    } else {
        -(heading ?: 0f)
    }
    // Main layout - centred and scrollable
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Back button
        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Subtitle
        Text(
            text = "LEADING YOU BACK TO",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selected memory
        Text(
            text = memory?.title ?: "No memory selected",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Compass dial and arrow
        // Box lets us put the arrow on top of the dial
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            // The dial turns with the phone so the directions stay correct
            CompassDial(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(4.dp, CircleShape)
                    .rotate(dialRotation)
            )
            // The arrow turns to point at the memory
            Image(
                painter = painterResource(
                    id = R.drawable.compass_arrow
                ),
                contentDescription = "Compass arrow",
                modifier = Modifier
                    .size(190.dp)
                    .rotate(compassResult?.arrowRotationDegrees ?: 0f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Location status
        // Shows the distance in metres, or a waiting message
        Text(
            text = if (compassResult != null) {
                String.format(
                    Locale.US,
                    "%.0f m",
                    compassResult.distanceMeters
                )
            } else {
                "Finding your location..."
            },
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Rounded tag that tells the user which direction to walk
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = if (compassResult != null) {
                    // The 8 main compass directions
                    val directions = listOf(
                        "N", "NE", "E", "SE",
                        "S", "SW", "W", "NW"
                    )

                    // Turn the bearing (0-360) into one of the 8 directions
                    // Each direction covers 45 degrees, +22.5 centres it
                    val index = (
                            (compassResult.bearingDegrees + 22.5f) / 45f
                            ).toInt() % 8

                    "Head ${directions[index]} · ${compassResult.distanceMeters.toInt()} m"
                } else {
                    "Waiting for location..."
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Information card
        // Gives the user a tip on how to use the compass
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                text = if (compassResult != null) {
                    "Turn your phone slowly. The arrow points towards the selected memory."
                } else {
                    "Waiting for location and orientation data..."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// Preview using the first sample memory (no sensor data, so it shows the waiting state)
@Preview(
    showBackground = true,
    widthDp = 393,
    heightDp = 850
)
@Composable
private fun MemoryCompassScreenPreview() {
    MemoryLaneTheme {
        MemoryCompassScreen(
            memory = SampleMemories.memories.first(),
            onBack = {}
        )
    }
}
