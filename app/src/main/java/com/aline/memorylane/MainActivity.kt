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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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

        val addMemoryButton = findViewById<Button>(R.id.buttonAddMemory)
        val viewAllButton = findViewById<Button>(R.id.buttonViewAll)
        val nearbyMemoryCard = findViewById<View>(R.id.cardNearbyMemory1)

        addMemoryButton.setOnClickListener {
            val intent = Intent(this, AddMemoryActivity::class.java)
            startActivity(intent)
        }

        viewAllButton.setOnClickListener {
            val intent = Intent(this, AllMemoriesActivity::class.java)
            startActivity(intent)
        }

        nearbyMemoryCard.setOnClickListener {
            val intent = Intent(this, MemoryDetailActivity::class.java)
            startActivity(intent)
        }
    }
}