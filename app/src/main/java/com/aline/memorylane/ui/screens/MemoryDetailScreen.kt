package com.aline.memorylane.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aline.memorylane.Memory
import com.aline.memorylane.SampleMemories
import com.aline.memorylane.ui.components.MemoryHeader
import com.aline.memorylane.ui.components.MemoryPhoto
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import java.util.Locale

// Screen that shows all the details of one memory
// From here the user can open maps, the compass, edit, delete or share the memory
@Composable
fun MemoryDetailScreen(
    memory: Memory,
    onBack: () -> Unit,
    onOpenMaps: () -> Unit,
    onOpenCompass: () -> Unit,
    onShare: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    distanceMeters: Float? = null
) {
    // True only if the memory has both latitude and longitude saved
    val hasLocation =
        memory.latitude != null && memory.longitude != null

    // Main layout - scrollable so all the content fits
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())

    ) {
        // Top bar with title and back button
        MemoryHeader(
            title = "Memory Detail",
            onBack = onBack
        )

        // Big photo of the memory at the top
        MemoryPhoto(
            photoUri = memory.photoUri,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Card with the main memory info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column {
                    // Memory title
                    Text(
                        text = memory.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Date and category next to each other
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = memory.date,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        // Category shown as a rounded tag
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = memory.category,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }

                    // Line to separate the info from the story
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp)
                    )

                    // Story section
                    Text(
                        text = "The Story",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Full description of the memory
                    Text(
                        text = memory.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Location section
            Text(
                text = "Location",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Show the coordinates if we have them, otherwise a message
            if (hasLocation) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Saved Location",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Latitude and longitude with 5 decimal places
                        Text(
                            text = String.format(
                                Locale.US,
                                "%.5f, %.5f",
                                memory.latitude,
                                memory.longitude
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                Text("Location not available yet")
            }

            // Show how far away the memory is (only if we know the distance)
            if (distanceMeters != null) {
                Spacer(modifier = Modifier.height(4.dp))

                // Under 1 km show metres, otherwise show km
                Text(
                    text = if (distanceMeters < 1000f) {
                        "${distanceMeters.toInt()} m away"
                    } else {
                        String.format(
                            Locale.US,
                            "%.1f km away",
                            distanceMeters / 1000f
                        )
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Maps and compass buttons only make sense if there is a location
            if (hasLocation) {
                // Opens the location in a maps app
                OutlinedButton(
                    onClick = onOpenMaps,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Open in Maps")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Opens the compass screen that points to the memory
                OutlinedButton(
                    onClick = onOpenCompass,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Open Memory Compass")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Edit button - goes to the edit screen
            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Memory")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Delete button
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Memory")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Share button (main button so it stands out)
            Button(
                onClick = onShare,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Share Memory")
            }
        }
    }
}

// Preview using the first sample memory (phone sized)
@Preview(showBackground = true, widthDp = 393, heightDp = 850)
@Composable
private fun MemoryDetailScreenPreview() {
    MemoryLaneTheme {
        MemoryDetailScreen(
            memory = SampleMemories.memories.first(),
            onBack = {},
            onOpenMaps = {},
            onOpenCompass = {},
            onShare = {},
            onEdit = {},
            onDelete = {}
        )
    }
}
