package com.aline.memorylane

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Main screen of the app (home page)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Let the app draw behind the status bar and navigation bar
        enableEdgeToEdge()
        // Load the layout for this screen
        setContentView(R.layout.activity_main)

        // Add padding so the content doesn't go under the system bars
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

        // Get the buttons and the card from the layout
        val addMemoryButton = findViewById<Button>(R.id.buttonAddMemory)
        val viewAllButton = findViewById<Button>(R.id.buttonViewAll)
        val nearbyMemoryCard = findViewById<View>(R.id.cardNearbyMemory1)

        // When "Add Memory" is clicked, open the Add Memory screen
        addMemoryButton.setOnClickListener {
            val intent = Intent(this, AddMemoryActivity::class.java)
            startActivity(intent)
        }

        // When "View All" is clicked, open the screen with all memories
        viewAllButton.setOnClickListener {
            val intent = Intent(this, AllMemoriesActivity::class.java)
            startActivity(intent)
        }

        // When the nearby memory card is clicked, open the memory details screen
        nearbyMemoryCard.setOnClickListener {
            val intent = Intent(this, MemoryDetailActivity::class.java)
            startActivity(intent)
        }
    }
}