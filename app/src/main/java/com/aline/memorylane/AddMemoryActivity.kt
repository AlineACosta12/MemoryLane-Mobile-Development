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

class AddMemoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val memoryToEdit = SampleMemories.memories.find {
            it.id == intent.getIntExtra("memory_id", -1)
        }
        setContent {
            MemoryLaneTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .systemBarsPadding()
                ) {
                    AddMemoryScreen(
                        onBack = {
                            finish()
                        },
                        memory = memoryToEdit,
                        onSave = { title, description, date, category, latitude, longitude, photoUri ->
                            val id = memoryToEdit?.id
                                ?: ((SampleMemories.memories.maxOfOrNull { it.id } ?: 0) + 1)
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
                            val index = SampleMemories.memories.indexOfFirst { it.id == id }
                            if (index == -1) {
                                SampleMemories.memories.add(memory)
                            } else {
                                SampleMemories.memories[index] = memory
                            }
                            Toast.makeText(
                                this@AddMemoryActivity,
                                if (memoryToEdit == null) "Memory saved!" else "Memory updated!",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        }
                    )
                }
            }
        }
    }
}
