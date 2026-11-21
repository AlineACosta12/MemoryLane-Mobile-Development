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
                        onSave = { title, description, date, category, latitude, longitude ->
                            val newId = (SampleMemories.memories.maxOfOrNull { it.id } ?: 0) + 1
                            val newMemory = Memory(
                                id = newId,
                                title = title,
                                description = description,
                                date = date,
                                category = category,
                                latitude = latitude,
                                longitude = longitude
                            )
                            SampleMemories.memories.add(newMemory)
                            Toast.makeText(
                                this@AddMemoryActivity,
                                "Memory saved!",
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
