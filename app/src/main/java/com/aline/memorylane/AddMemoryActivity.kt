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
                        onSave = { title, description, date, category ->

                            if (
                                title.isBlank() ||
                                description.isBlank() ||
                                date.isBlank() ||
                                category.isBlank()
                            ) {
                                Toast.makeText(
                                    this@AddMemoryActivity,
                                    "Please complete all fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@AddMemoryActivity,
                                    "Memory details are ready to save",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }
}
