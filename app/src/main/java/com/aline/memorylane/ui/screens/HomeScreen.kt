package com.aline.memorylane.ui.screens

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aline.memorylane.Memory
import com.aline.memorylane.R
import com.aline.memorylane.SampleMemories
import com.aline.memorylane.ui.components.MemoryPhoto
import com.aline.memorylane.ui.theme.CardTitleStyle
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import com.aline.memorylane.ui.theme.SectionTitleStyle

// Main home screen of the app
// Shows memories near the user, recently added memories and buttons to add/view memories
@Composable
fun HomeScreen(
    onAddMemory: () -> Unit,
    onViewAll: () -> Unit,
    onOpenMemory: (Int) -> Unit,
    currentLocation: Location? = null
) {
    // Work out how far each memory is from the user's current location
    // If we don't have a location yet, this is just an empty list
    val memoryDistances = currentLocation?.let { location ->
        SampleMemories.memories.mapNotNull { memory ->
            // Skip memories that don't have a location saved
            val lat = memory.latitude ?: return@mapNotNull null
            val lon = memory.longitude ?: return@mapNotNull null

            // Make a Location object for the memory so we can measure distance
            val destination = Location("memory").apply {
                latitude = lat
                longitude = lon
            }
            // Pair each memory with its distance in metres
            memory to location.distanceTo(destination)
        }
    } ?: emptyList()
    // Memories within 2 km of the user
    val nearbyMemories = memoryDistances
        .filter { it.second <= 2000f }
        .map { it.first }
    // First memory within 150 m - means the user is back at that place
    val returnedMemory = memoryDistances
        .firstOrNull { it.second <= 150f }
        ?.first

    // Main layout - scrollable so all sections fit
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        // Big image header at the top
        HomeHeader()
        // Content area - moved up a bit so it overlaps the header with rounded corners
        Column(
            Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                )

                .padding(16.dp)
                .navigationBarsPadding(),
        ) {
            // "Around You" section - memories close to the user
            Text(
                text = "Around You",
                style = SectionTitleStyle,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(8.dp))

            // Show a different message depending on the location state
            if (currentLocation == null) {
                Text("Waiting for current location...")
            } else if (nearbyMemories.isEmpty()) {
                Text("No memories nearby.")
            } else {
                // Extra message if the user is right at a memory's place
                if (returnedMemory != null) {
                    Text(
                        text = "You're Back Here!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "You're near ${returnedMemory.title}.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // List of nearby memory cards
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    nearbyMemories.forEach { memory ->
                        MemoryCard(
                            memory = memory,
                            onOpen = { onOpenMemory(memory.id) }
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))

            // "Recently Added" section
            Text(
                text = "Recently Added",
                style = SectionTitleStyle,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(10.dp))

            // Show all memories, newest first (reversed list)
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SampleMemories.memories.asReversed().forEach { memory ->
                    MemoryCard(
                        memory = memory,
                        onOpen = { onOpenMemory(memory.id) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Button to go to the Add Memory screen
            Button(
                onClick = onAddMemory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Memory")
            }

            Spacer(Modifier.height(8.dp))

            // Button to see the full list of memories
            OutlinedButton(
                onClick = onViewAll,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Memories")
            }

        }
    }
}

// Small card that shows one memory (photo, title, category and date)
// Tapping the card opens the memory
@Composable
private fun MemoryCard(
    memory: Memory,
    onOpen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )

    ) {
        // Photo on the left, text on the right
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Memory photo with rounded corners
            MemoryPhoto(
                photoUri = memory.photoUri,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column {
                Text(
                    text = memory.title,
                    style = CardTitleStyle
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Category and date on one line
                Text(
                    text = "${memory.category} · ${memory.date}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// Header at the top of the home screen with a background image, logo, app name and tagline
@Composable
private fun HomeHeader() {
    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 250.dp),
    ) {
        // Background landscape image
        Image(
            painter = painterResource(R.drawable.landscape),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
        // Darkens the top and bottom so the white text stays readable.
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xA61F3A26), Color(0x331F3A26), Color(0x8C1F3A26)),
                    ),
                ),
        )

        // Logo and text in the middle of the header
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // App logo
            Image(
                painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.size(52.dp)
            )
            // App name
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier.padding(top = 6.dp),
            )
            // Tagline (slightly see-through white)
            Text(
                stringResource(R.string.tagline),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}


// Preview of just the header in Android Studio
@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    MemoryLaneTheme {
        HomeHeader()
    }
}

// Preview of the full home screen (phone sized)
@Preview(
    showBackground = true,
    widthDp = 393,
    heightDp = 850
)
@Composable
private fun HomeScreenPreview() {
    MemoryLaneTheme {
        HomeScreen(
            onAddMemory = {},
            onViewAll = {},
            onOpenMemory = {}
        )
    }
}

