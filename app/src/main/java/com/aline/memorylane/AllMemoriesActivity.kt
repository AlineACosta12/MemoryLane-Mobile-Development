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

class AllMemoriesActivity : AppCompatActivity() {

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
                    AllMemoriesScreen(
                        onBack = {
                            finish()
                        },
                        onOpenMemory = { memoryId ->
                            val intent = Intent(
                                this@AllMemoriesActivity,
                                MemoryDetailActivity::class.java
                            )

                            intent.putExtra("memory_id", memoryId)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
