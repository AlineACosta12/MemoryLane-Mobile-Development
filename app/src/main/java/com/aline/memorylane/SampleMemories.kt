package com.aline.memorylane

import androidx.compose.runtime.mutableStateListOf

object SampleMemories {
    // Sample memories used until persistent storage is implemented
    val memories = mutableStateListOf(
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
}