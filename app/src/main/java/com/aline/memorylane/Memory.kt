package com.aline.memorylane

// Data class that holds all the information about one memory
data class Memory(

    // Unique number for each memory (0 by default)
    val id: Int = 0,

    // Title of the memory
    val title: String,

    // Short text describing the memory
    val description: String,

    // Date when the memory happened
    val date: String,

    // Category chosen by the user
    val category: String,

    // Location of the memory (can be null if no location was added)
    val latitude: Double? = null,
    val longitude: Double? = null,

    // Distance in metres to remind the user of this memory when nearby
    val rediscoveryRadius: Int = 500,

    // True if the user marked this memory as a favourite
    val isFavourite: Boolean = false,
    val photoUri: String? = null
)