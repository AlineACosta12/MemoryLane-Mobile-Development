package com.aline.memorylane

import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.aline.memorylane.sensors.location.LocationHelper
import com.aline.memorylane.ui.screens.MemoryDetailScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme

// Activity that shows the details of one memory
// Handles edit, delete, open in maps, open compass and share
class MemoryDetailActivity : AppCompatActivity() {
    // Opens the edit screen, and when the user comes back,
    // recreate() reloads this screen so it shows the updated memory
    private val editMemoryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Let the app draw behind the status bar and navigation bar
        enableEdgeToEdge()

        // Find the selected memory using its ID
        val memoryId = intent.getIntExtra(
            "memory_id",
            intent.getIntExtra("memoryId", -1)
        )

        val selectedMemory = SampleMemories.memories.find {
            it.id == memoryId
        }

        // Support the previous navigation format too
        // If the memory wasn't found by id, build it from the extras that were sent
        // If there are no extras either, just show the first sample memory
        val memory = selectedMemory ?: if (intent.hasExtra("memoryTitle")) {
            Memory(
                id = memoryId,
                title = intent.getStringExtra("memoryTitle").orEmpty(),
                description = intent.getStringExtra("memoryDescription").orEmpty(),
                date = intent.getStringExtra("memoryDate").orEmpty(),
                category = intent.getStringExtra("memoryCategory").orEmpty(),
                // Only use the coordinates if they were actually sent
                latitude = if (intent.hasExtra("memoryLatitude")) {
                    intent.getDoubleExtra("memoryLatitude", 0.0)
                } else {
                    null
                },
                longitude = if (intent.hasExtra("memoryLongitude")) {
                    intent.getDoubleExtra("memoryLongitude", 0.0)
                } else {
                    null
                }
            )
        } else {
            SampleMemories.memories.first()
        }

        // Distance from the user to the memory (null until we know it)
        val distance = mutableStateOf<Float?>(null)

        // Get the user's location and work out how far away the memory is
        LocationHelper.getCurrentLocation(this) { currentLocation ->
            val lat = memory.latitude
            val lon = memory.longitude

            // Only calculate if we have both the user's location and the memory's location
            if (currentLocation != null && lat != null && lon != null) {
                val destination = Location("memory").apply {
                    latitude = lat
                    longitude = lon
                }

                // Distance in metres - updating this refreshes the screen
                distance.value = currentLocation.distanceTo(destination)
            }
        }

        setContent {
            MemoryLaneTheme {
                // systemBarsPadding stops the content going under the system bars
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .systemBarsPadding()
                ) {
                    MemoryDetailScreen(
                        memory = memory,
                        distanceMeters = distance.value,
                        // Open the Add Memory screen in edit mode
                        onEdit = {
                            val intent = Intent(
                                this@MemoryDetailActivity,
                                AddMemoryActivity::class.java
                            )
                            // Send the id so the edit screen loads this memory
                            intent.putExtra("memory_id", memory.id)
                            editMemoryLauncher.launch(intent)
                        },
                        // Close this activity and go back
                        onBack = {
                            finish()
                        },

                        // Ask the user to confirm before deleting
                        onDelete = {
                            AlertDialog.Builder(this@MemoryDetailActivity)
                                .setTitle("Delete memory?")
                                .setMessage("Are you sure you want to delete this memory?")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("Delete") { _, _ ->
                                    // Remove the memory from the list and close the screen
                                    SampleMemories.memories.removeAll {
                                        it.id == memory.id
                                    }
                                    finish()
                                }
                                .show()
                        },

                        // Open the memory's location in a maps app
                        onOpenMaps = {
                            val latitude = memory.latitude
                            val longitude = memory.longitude

                            if (latitude != null && longitude != null) {
                                // Encode the title so spaces and symbols work in the link
                                val encodedTitle = Uri.encode(memory.title)

                                // geo: link with a pin labelled with the memory title
                                val mapUri = Uri.parse(
                                    "geo:$latitude,$longitude" +
                                            "?q=$latitude,$longitude($encodedTitle)"
                                )

                                val mapIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    mapUri
                                )

                                // Show a message instead of crashing if no maps app is installed
                                try {
                                    startActivity(mapIntent)
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(
                                        this@MemoryDetailActivity,
                                        "No maps application found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        },

                        // Open the compass screen for this memory
                        onOpenCompass = {
                            val compassIntent = Intent(
                                this@MemoryDetailActivity,
                                MemoryCompassActivity::class.java
                            )

                            compassIntent.putExtra("memory_id", memory.id)
                            startActivity(compassIntent)
                        },

                        // Share the memory as text with other apps
                        onShare = {
                            // Text that will be shared
                            val shareText = """
                                Memory Lane

                                ${memory.title}
                                ${memory.date}
                                ${memory.category}

                                ${memory.description}
                            """.trimIndent()

                            // ACTION_SEND lets the user send plain text to another app
                            val shareIntent = Intent(
                                Intent.ACTION_SEND
                            ).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    shareText
                                )
                            }

                            // Show the share menu so the user can pick an app
                            startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    "Share memory"
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
