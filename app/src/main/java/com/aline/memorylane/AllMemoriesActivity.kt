package com.aline.memorylane

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllMemoriesActivity : AppCompatActivity() {

    // Adapter for the memory list
    private lateinit var memoryAdapter: MemoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_memories)

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

        // Get the RecyclerView and category filter from the layout
        val recyclerMemories =
            findViewById<RecyclerView>(R.id.recyclerMemories)

        val spinnerMemoryFilter =
            findViewById<Spinner>(R.id.spinnerMemoryFilter)

        // Sample memories used until persistent storage is implemented
        val memories = listOf(
            Memory(
                id = 1,
                title = "First Coffee Together",
                description = "Our first coffee together after a long walk around the city.",
                date = "January 2025",
                category = "Together",
                latitude = 51.8985,
                longitude = -8.4756
            ),

            Memory(
                id = 2,
                title = "Malta",
                description = "A special trip and one of our favourite adventures.",
                date = "September 2026",
                category = "Adventure"
            ),

            Memory(
                id = 3,
                title = "Mobile Development Project",
                description = "Started developing the Memory Lane Android application.",
                date = "November 2026",
                category = "Milestone"
            )
        )

        // Create the adapter and define what happens when a memory is selected
        memoryAdapter = MemoryAdapter(memories) { memory ->

            val intent =
                Intent(this, MemoryDetailActivity::class.java)

            // Pass the selected memory information to the detail screen
            intent.putExtra("memoryId", memory.id)
            intent.putExtra("memoryTitle", memory.title)
            intent.putExtra("memoryDescription", memory.description)
            intent.putExtra("memoryDate", memory.date)
            intent.putExtra("memoryCategory", memory.category)

            // Send location only when the memory has coordinates
            memory.latitude?.let {
                intent.putExtra("memoryLatitude", it)
            }

            memory.longitude?.let {
                intent.putExtra("memoryLongitude", it)
            }

            startActivity(intent)
        }

        // Display memories as a vertical scrolling list
        recyclerMemories.layoutManager =
            LinearLayoutManager(this)

        recyclerMemories.adapter =
            memoryAdapter

        // Categories available in the filter
        val categories = arrayOf(
            "All categories",
            "Together",
            "Adventure",
            "Milestone",
            "Little Moments",
            "Celebration",
            "Special Place"
        )

        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        spinnerMemoryFilter.adapter =
            categoryAdapter

        // Filter the displayed memories when a category is selected
        spinnerMemoryFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCategory =
                        categories[position]

                    if (selectedCategory == "All categories") {
                        memoryAdapter.updateMemories(memories)

                    } else {

                        val filteredMemories =
                            memories.filter { memory ->
                                memory.category == selectedCategory
                            }
                        memoryAdapter.updateMemories(filteredMemories)
                    }
                }
                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                    // No action is required
                }
            }
    }
}