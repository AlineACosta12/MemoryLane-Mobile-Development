package com.aline.memorylane.ui.screens

import androidx.compose.ui.tooling.preview.Preview
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aline.memorylane.Memory
import com.aline.memorylane.R
import com.aline.memorylane.ui.theme.CardTitleStyle
import com.aline.memorylane.ui.theme.SectionTitleStyle
import com.aline.memorylane.SampleMemories
import android.location.Location
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.draw.clip
import com.aline.memorylane.ui.components.MemoryPhoto

@Composable
fun HomeScreen(
    onAddMemory: () -> Unit,
    onViewAll: () -> Unit,
    onOpenMemory: (Int) -> Unit,
    currentLocation: Location? = null
){
    val memoryDistances = currentLocation?.let { location ->
        SampleMemories.memories.mapNotNull { memory ->
            val lat = memory.latitude ?: return@mapNotNull null
            val lon = memory.longitude ?: return@mapNotNull null

            val destination = Location("memory").apply {
                latitude = lat
                longitude = lon
            }
            memory to location.distanceTo(destination)
        }
    } ?: emptyList()
    val nearbyMemories = memoryDistances
        .filter { it.second <= 2000f }
        .map { it.first }
    val returnedMemory = memoryDistances
        .firstOrNull { it.second <= 150f }
        ?.first

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        HomeHeader()
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
            Text(
                text = "Around You",
                style = SectionTitleStyle,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(8.dp))

            if (currentLocation == null) {
                Text("Waiting for current location...")
            } else if (nearbyMemories.isEmpty()) {
                Text("No memories nearby.")
            } else {
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

            Text(
                text = "Recently Added",
                style = SectionTitleStyle,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(10.dp))

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

            Button(
                onClick = onAddMemory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Memory")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = onViewAll,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Memories")
            }

        }
    }
}

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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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

                Text(
                    text = "${memory.category} · ${memory.date}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
@Composable
private fun HomeHeader() {
    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 250.dp),
    ) {
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

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(painterResource(R.drawable.ic_logo), contentDescription = null, modifier = Modifier.size(52.dp))
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                stringResource(R.string.tagline),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    MemoryLaneTheme {
        HomeHeader()
    }
}

@Preview(
    showBackground = true,
    widthDp = 393,
    heightDp = 850
)
@Composable
private fun HomeScreenPreview(){
    MemoryLaneTheme {
        HomeScreen(
            onAddMemory = {},
            onViewAll = {},
            onOpenMemory = {}
        )
    }
}

