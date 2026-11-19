package com.aline.memorylane

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MemoryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_memory_detail)

        // Add padding so the content does not go under the system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Get the TextViews from the layout
        val titleText =
            findViewById<TextView>(R.id.textMemoryTitle)
        val dateText =
            findViewById<TextView>(R.id.textMemoryDate)
        val categoryText =
            findViewById<TextView>(R.id.textMemoryCategory)
        val descriptionText =
            findViewById<TextView>(R.id.textMemoryDescription)
        val distanceText =
            findViewById<TextView>(R.id.textMemoryDistance)

        // Read the selected memory information sent by the previous Activity
        // Default values are used when the memory is opened directly from the Home screen
        val memoryId = intent.getIntExtra("memory_id", -1)
        val selectedMemory = SampleMemories.memories.find {
            it.id == memoryId
        }
        val memoryTitle = selectedMemory?.title
            ?: intent.getStringExtra("memoryTitle")
            ?: "First Coffee Together"
        val memoryDescription = selectedMemory?.description
            ?: intent.getStringExtra("memoryDescription")
            ?: "Our first coffee together after a long walk around the city."
        val memoryDate = selectedMemory?.date
            ?: intent.getStringExtra("memoryDate")
            ?: "January 2025"
        val memoryCategory = selectedMemory?.category
            ?: intent.getStringExtra("memoryCategory")
            ?: "Together"

        /*
         * The Home screen currently opens First Coffee Together without extras,
         * so its sample coordinates are used as a fallback.
         */
        val openedWithoutExtras =
            !intent.hasExtra("memory_id") &&
                    !intent.hasExtra("memoryTitle")
        val latitude = when {
            selectedMemory != null -> selectedMemory.latitude
            intent.hasExtra("memoryLatitude") ->
                intent.getDoubleExtra("memoryLatitude", 0.0)
            openedWithoutExtras -> 51.8985
            else -> null
        }
        val longitude = when {
            selectedMemory != null -> selectedMemory.longitude
            intent.hasExtra("memoryLongitude") ->
                intent.getDoubleExtra("memoryLongitude", 0.0)
            openedWithoutExtras -> -8.4756
            else -> null
        }

        val hasLocation = latitude != null && longitude != null

        // Display the selected memory information
        titleText.text = memoryTitle
        dateText.text = memoryDate
        categoryText.text = memoryCategory
        descriptionText.text = memoryDescription

        if (hasLocation) {
            distanceText.text = "Saved location"
        } else {
            distanceText.text = "Location not available yet"
        }

        // OPEN IN MAPS
        val openMapsButton =
            findViewById<Button>(R.id.buttonOpenMaps)
        if (hasLocation) {
            openMapsButton.visibility = View.VISIBLE
            openMapsButton.setOnClickListener {
                val encodedTitle =
                    Uri.encode(memoryTitle)
                val mapUri = Uri.parse(
                    "geo:$latitude,$longitude" +
                            "?q=$latitude,$longitude($encodedTitle)"
                )
                val mapIntent =
                    Intent(Intent.ACTION_VIEW, mapUri)
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
        } else {
            // Hide Maps when the selected memory has no saved coordinates
            openMapsButton.visibility = View.GONE
        }
        // SHARE MEMORY
        val shareButton =
            findViewById<Button>(R.id.buttonShareMemory)
        shareButton.setOnClickListener {
            val shareText = """
                Memory Lane
             
                $memoryTitle
                $memoryDate
                $memoryCategory
                
                $memoryDescription
            """.trimIndent()

            val shareIntent =
                Intent(Intent.ACTION_SEND).apply {
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
    }
}