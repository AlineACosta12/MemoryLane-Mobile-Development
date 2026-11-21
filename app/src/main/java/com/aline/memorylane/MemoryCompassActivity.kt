package com.aline.memorylane

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.aline.memorylane.ui.screens.MemoryCompassScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme

class MemoryCompassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get the memory selected on the previous screen
        val memoryId = intent.getIntExtra(
            "memory_id",
            intent.getIntExtra("memoryId", -1)
        )

        val selectedMemory = SampleMemories.memories.find {
            it.id == memoryId
        }

        setContent {
            MemoryLaneTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .systemBarsPadding()
                ) {
                    MemoryCompassScreen(
                        memory = selectedMemory,
                        onBack = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}
