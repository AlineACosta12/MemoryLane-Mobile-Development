package com.aline.memorylane.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aline.memorylane.SampleMemories
import com.aline.memorylane.ui.components.MemoryHeader
import com.aline.memorylane.ui.components.MemoryPhoto
import com.aline.memorylane.ui.theme.MemoryLaneTheme

// Screen that shows a list of all saved memories
// The user can filter the list by category and tap a memory to open it
@Composable
fun AllMemoriesScreen(
    onBack: () -> Unit,
    onOpenMemory: (Int) -> Unit
) {
    // The category currently picked in the filter (starts with all of them)
    var selectedCategory by rememberSaveable {
        mutableStateOf("All categories")
    }

    // Controls if the filter dropdown is open or closed
    var filterExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    // Options for the filter dropdown
    val categories = listOf(
        "All categories",
        "Together",
        "Adventure",
        "Milestone",
        "Little Moments",
        "Celebration",
        "Special Place"
    )

    // Only keep memories that match the chosen category
    // If "All categories" is picked, show everything
    val filteredMemories = SampleMemories.memories.filter { memory ->
        selectedCategory == "All categories" ||
                memory.category == selectedCategory
    }

    // Main layout of the screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Top bar with title and back button
        MemoryHeader(
            title = "All Memories",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Every place has a story.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Category filter
            Box(modifier = Modifier.fillMaxWidth()) {

                // Read only field that shows the selected category
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    label = { Text("Filter by category") },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        // Opens the dropdown
                        TextButton(
                            onClick = { filterExpanded = true }
                        ) {
                            Text("Choose")
                        }
                    }
                )

                // Dropdown with all the categories
                DropdownMenu(
                    expanded = filterExpanded,
                    onDismissRequest = {
                        filterExpanded = false
                    }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                // Update the filter and close the menu
                                selectedCategory = category
                                filterExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Scrollable memory list
            // LazyColumn only draws the items on screen, so it works well for long lists
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Show a message if no memories match the filter
                if (filteredMemories.isEmpty()) {
                    item {
                        Text(
                            text = "No memories found in this category.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        )
                    }
                }

                // One card for each memory (id is used as a unique key)
                items(
                    items = filteredMemories,
                    key = { it.id }
                ) { memory ->

                    // Memory card - tapping it opens the memory details
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOpenMemory(memory.id)
                            },

                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
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
                            // Text takes up the rest of the space
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = memory.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Category and date on one line
                                Text(
                                    text = "${memory.category} · ${memory.date}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Short preview of the description (max 2 lines)
                                Text(
                                    text = memory.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Preview of the screen in Android Studio (phone sized)
@Preview(showBackground = true, widthDp = 393, heightDp = 850)
@Composable
private fun AllMemoriesScreenPreview() {
    MemoryLaneTheme {
        AllMemoriesScreen(
            onBack = {},
            onOpenMemory = {}
        )
    }
}
