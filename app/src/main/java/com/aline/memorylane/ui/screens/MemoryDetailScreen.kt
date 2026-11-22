package com.aline.memorylane.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aline.memorylane.Memory
import com.aline.memorylane.SampleMemories
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import java.util.Locale
import com.aline.memorylane.ui.components.MemoryPhoto
import com.aline.memorylane.ui.components.MemoryHeader

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
    val hasLocation =
        memory.latitude != null && memory.longitude != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())

    ) {
        MemoryHeader(
            title = "Memory Detail",
            onBack = onBack
        )

        MemoryPhoto(
            photoUri = memory.photoUri,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                 Column {
                    Text(
                        text = memory.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                     Row(
                         verticalAlignment = Alignment.CenterVertically,
                         horizontalArrangement = Arrangement.spacedBy(12.dp)
                     ) {
                         Text(
                             text = memory.date,
                             style = MaterialTheme.typography.bodyMedium
                         )

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

                     HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp)
                    )

                    Text(
                        text = "The Story",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = memory.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Location",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

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

            if (distanceMeters != null) {
                Spacer(modifier = Modifier.height(4.dp))

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

            if (hasLocation) {
                OutlinedButton(
                    onClick = onOpenMaps,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Open in Maps")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onOpenCompass,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Open Memory Compass")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Memory")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Memory")
            }

            Spacer(modifier = Modifier.height(12.dp))

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
