package com.aline.memorylane.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Reusable top bar used on most screens
// Shows a back button and the screen title
@Composable
fun MemoryHeader(
    title: String,
    onBack: () -> Unit
) {
    // Full width bar with the app's main colour as background
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button - goes to the previous screen
        TextButton(onClick = onBack) {
            Text("← Back", color = Color.White)
        }

        // Space between the button and the title
        Spacer(modifier = Modifier.width(12.dp))

        // Screen title (white so it shows on the coloured background)
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}
