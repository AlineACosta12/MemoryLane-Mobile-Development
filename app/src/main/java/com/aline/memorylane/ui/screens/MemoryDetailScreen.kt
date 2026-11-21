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

@Composable
fun MemoryDetailScreen(
    memory: Memory,
    onBack: () -> Unit,
    onOpenMaps: () -> Unit,
    onOpenCompass: () -> Unit,
    onShare: () -> Unit
) {
    val hasLocation =
        memory.latitude != null && memory.longitude != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Memory Details",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = memory.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = memory.category,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = memory.date,
                    style = MaterialTheme.typography.bodyMedium
                )

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

        Text(
            text = if (hasLocation) {
                "Saved location"
            } else {
                "Location not available yet"
            },
            style = MaterialTheme.typography.bodyMedium
        )

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

@Preview(showBackground = true, widthDp = 393, heightDp = 850)
@Composable
private fun MemoryDetailScreenPreview() {
    MemoryLaneTheme {
        MemoryDetailScreen(
            memory = SampleMemories.memories.first(),
            onBack = {},
            onOpenMaps = {},
            onOpenCompass = {},
            onShare = {}
        )
    }
}
