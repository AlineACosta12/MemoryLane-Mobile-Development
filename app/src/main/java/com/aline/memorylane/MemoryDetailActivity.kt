package com.aline.memorylane

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.ActivityNotFoundException

class MemoryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_memory_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val openMapsButton = findViewById<Button>(R.id.buttonOpenMaps)

        openMapsButton.setOnClickListener {

            val latitude = 51.8985
            val longitude = -8.4756
            val memoryTitle = "First Coffee Together"

            val mapUri = Uri.parse(
                "geo:$latitude,$longitude?q=$latitude,$longitude($memoryTitle)"
            )

            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)

            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "No maps application found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val shareButton = findViewById<Button>(R.id.buttonShareMemory)
        shareButton.setOnClickListener {

            val shareText = """
        Memory Lane

        First Coffee Together
        January 2025
        Together

        Our first coffee together after a long walk around the city.
    """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share memory"
                )
            )
        }
    }
}