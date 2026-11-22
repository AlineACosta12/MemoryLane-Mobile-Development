package com.aline.memorylane

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
import com.aline.memorylane.ui.screens.AddMemoryScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme

// Activity for adding a new memory or editing an existing one
class AddMemoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Let the app draw behind the status bar and navigation bar
        enableEdgeToEdge()
        // If a memory id was passed in, find that memory so we can edit it
        // If not, this will be null and we are adding a new memory
        val memoryToEdit = SampleMemories.memories.find {
            it.id == intent.getIntExtra("memory_id", -1)
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
                    AddMemoryScreen(
                        // Close this activity and go back
                        onBack = {
                            finish()
                        },
                        memory = memoryToEdit,
                        // Runs when the user presses Save and all fields are valid
                        onSave = { title, description, date, category, latitude, longitude, photoUri ->
                            // Keep the same id when editing, otherwise make a new id
                            val id = memoryToEdit?.id
                                ?: ((SampleMemories.memories.maxOfOrNull { it.id } ?: 0) + 1)
                            // Create the memory with the new data
                            // (keep the old radius and favourite values if editing)
                            val memory = Memory(
                                id = id,
                                title = title,
                                description = description,
                                date = date,
                                category = category,
                                latitude = latitude,
                                longitude = longitude,
                                rediscoveryRadius = memoryToEdit?.rediscoveryRadius ?: 500,
                                isFavourite = memoryToEdit?.isFavourite ?: false,
                                photoUri = photoUri
                            )
                            // Check if this memory is already in the list
                            val index = SampleMemories.memories.indexOfFirst { it.id == id }
                            if (index == -1) {
                                // Not in the list - add it as a new memory
                                SampleMemories.memories.add(memory)
                            } else {
                                // Already in the list - replace the old version
                                SampleMemories.memories[index] = memory
                            }
                            // Show a short message to the user
                            Toast.makeText(
                                this@AddMemoryActivity,
                                if (memoryToEdit == null) "Memory saved!" else "Memory updated!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Close the screen and go back
                            finish()
                        }
                    )
                }
            }
        }
    }
}
