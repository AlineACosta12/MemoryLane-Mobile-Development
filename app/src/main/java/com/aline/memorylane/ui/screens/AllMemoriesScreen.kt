package com.aline.memorylane.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aline.memorylane.SampleMemories
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.aline.memorylane.ui.components.MemoryPhoto
import com.aline.memorylane.ui.components.MemoryHeader

@Composable
fun AllMemoriesScreen(
    onBack: () -> Unit,
    onOpenMemory: (Int) -> Unit
) {
    var selectedCategory by rememberSaveable {
        mutableStateOf("All categories")
    }

    var filterExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val categories = listOf(
        "All categories",
        "Together",
        "Adventure",
        "Milestone",
        "Little Moments",
        "Celebration",
        "Special Place"
    )

    val filteredMemories = SampleMemories.memories.filter { memory ->
        selectedCategory == "All categories" ||
                memory.category == selectedCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

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

                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    label = { Text("Filter by category") },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        TextButton(
                            onClick = { filterExpanded = true }
                        ) {
                            Text("Choose")
                        }
                    }
                )

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
                                selectedCategory = category
                                filterExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Scrollable memory list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
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

                items(
                    items = filteredMemories,
                    key = { it.id }
                ) { memory ->

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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = memory.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${memory.category} · ${memory.date}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.height(6.dp))

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
