package com.aline.memorylane.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aline.memorylane.Memory
import com.aline.memorylane.R
import com.aline.memorylane.SampleMemories
import com.aline.memorylane.ui.components.CompassDial
import com.aline.memorylane.ui.theme.MemoryLaneTheme

@Composable
fun MemoryCompassScreen(
    memory: Memory?,
    onBack: () -> Unit
) {
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
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {

            CompassDial(
                modifier = Modifier.fillMaxSize()
            )

            Image(
                painter = painterResource(
                    id = R.drawable.compass_arrow
                ),
                contentDescription = "Compass arrow",
                modifier = Modifier.size(190.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Location status
        Text(
            text = "Direction pending",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = "GPS and sensors not connected",
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
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                text = "Your compass will point towards a saved memory when location and orientation sensors are connected.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

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
