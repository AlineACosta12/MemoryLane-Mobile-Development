package com.aline.memorylane

import android.content.Intent
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
import com.aline.memorylane.ui.screens.AllMemoriesScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme

// Activity that shows the list of all memories
class AllMemoriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Let the app draw behind the status bar and navigation bar
        enableEdgeToEdge()

        setContent {
            MemoryLaneTheme {
                // systemBarsPadding stops the content going under the system bars
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .systemBarsPadding()
                ) {
                    AllMemoriesScreen(
                        // Close this activity and go back
                        onBack = {
                            finish()
                        },
                        // When a memory is tapped, open its detail screen
                        onOpenMemory = { memoryId ->
                            val intent = Intent(
                                this@AllMemoriesActivity,
                                MemoryDetailActivity::class.java
                            )

                            // Send the memory id so the detail screen knows which one to show
                            intent.putExtra("memory_id", memoryId)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
