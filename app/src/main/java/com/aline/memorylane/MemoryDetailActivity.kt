package com.aline.memorylane

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.aline.memorylane.ui.screens.MemoryDetailScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class MemoryDetailActivity : AppCompatActivity() {
    private val editMemoryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        recreate()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val memory = selectedMemory ?: if (intent.hasExtra("memoryTitle")) {
            Memory(
                id = memoryId,
                title = intent.getStringExtra("memoryTitle").orEmpty(),
                description = intent.getStringExtra("memoryDescription").orEmpty(),
                date = intent.getStringExtra("memoryDate").orEmpty(),
                category = intent.getStringExtra("memoryCategory").orEmpty(),
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

        setContent {
            MemoryLaneTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .systemBarsPadding()
                ) {
                    MemoryDetailScreen(
                        memory = memory,
                        onEdit = {
                            val intent = Intent(
                                this@MemoryDetailActivity,
                                AddMemoryActivity::class.java
                            )
                            intent.putExtra("memory_id", memory.id)
                            editMemoryLauncher.launch(intent)
                        },
                        onBack = {
                            finish()
                        },

                        onDelete = {
                            AlertDialog.Builder(this@MemoryDetailActivity)
                                .setTitle("Delete memory?")
                                .setMessage("Are you sure you want to delete this memory?")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("Delete") { _, _ ->
                                    SampleMemories.memories.removeAll {
                                        it.id == memory.id
                                    }
                                    finish()
                                }
                                .show()
                        },

                        onOpenMaps = {
                            val latitude = memory.latitude
                            val longitude = memory.longitude

                            if (latitude != null && longitude != null) {
                                val encodedTitle = Uri.encode(memory.title)

                                val mapUri = Uri.parse(
                                    "geo:$latitude,$longitude" +
                                            "?q=$latitude,$longitude($encodedTitle)"
                                )

                                val mapIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    mapUri
                                )

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

                        onOpenCompass = {
                            val compassIntent = Intent(
                                this@MemoryDetailActivity,
                                MemoryCompassActivity::class.java
                            )

                            compassIntent.putExtra("memory_id", memory.id)
                            startActivity(compassIntent)
                        },

                        onShare = {
                            val shareText = """
                                Memory Lane

                                ${memory.title}
                                ${memory.date}
                                ${memory.category}

                                ${memory.description}
                            """.trimIndent()

                            val shareIntent = Intent(
                                Intent.ACTION_SEND
                            ).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    shareText
                                )
                            }

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
