package com.aline.memorylane

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class AddMemoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Let the app draw behind the status bar and navigation bar
        enableEdgeToEdge()
        // Load the layout for this screen
        setContentView(R.layout.activity_add_memory)

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

        // DATE
        // Get the date field from the layout
        val dateInput = findViewById<EditText>(R.id.editMemoryDate)

        // When the date field is clicked, show a calendar to pick a date
        dateInput.setOnClickListener {

            // Get today's date so the calendar starts on today
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create the date picker pop-up
            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    // Months start at 0, so we add 1 to show the correct month
                    val selectedDate =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"

                    // Show the chosen date in the date field
                    dateInput.setText(selectedDate)
                },
                year,
                month,
                day
            )

            // Show the date picker on the screen
            datePicker.show()
        }

        // CATEGORY
        // Get the dropdown (spinner) for the category
        val categorySpinner = findViewById<Spinner>(R.id.spinnerCategory)

        // List of categories the user can choose from
        val categories = arrayOf(
            "Select category",
            "Together",
            "Adventure",
            "Milestone",
            "Little Moments",
            "Celebration",
            "Special Place"
        )

        // Adapter connects the list of categories to the spinner
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )

        // Set how the dropdown list looks when it opens
        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        // Attach the adapter to the spinner
        categorySpinner.adapter = categoryAdapter

        // SAVE
        // Get the other views from the layout
        val saveButton = findViewById<Button>(R.id.buttonSaveMemory)
        val titleInput = findViewById<EditText>(R.id.editMemoryTitle)
        val descriptionInput = findViewById<EditText>(R.id.editMemoryDescription)
        val locationButton = findViewById<Button>(R.id.buttonUseLocation)
        val locationStatus = findViewById<TextView>(R.id.textLocationStatus)

        // Location button (GPS is not added yet, so just show a message)
        locationButton.setOnClickListener {

            // Update the text under the button
            locationStatus.text = "Location integration planned for Milestone 2"

            // Show a short pop-up message
            Toast.makeText(
                this,
                "GPS will be connected in the next milestone",
                Toast.LENGTH_SHORT
            ).show()
        }

        // When the Save button is clicked, check that all fields are filled in
        saveButton.setOnClickListener {

            // Get the text the user typed (trim removes extra spaces)
            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val date = dateInput.text.toString().trim()

            // Check the title is not empty
            if (title.isEmpty()) {
                titleInput.error = "Please enter a memory title"
                return@setOnClickListener
            }

            // Check the description is not empty
            if (description.isEmpty()) {
                descriptionInput.error = "Please enter a description"
                return@setOnClickListener
            }

            // Check a date was picked
            if (date.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please select a date",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Check a real category was picked (position 0 is "Select category")
            if (categorySpinner.selectedItemPosition == 0) {

                Toast.makeText(
                    this,
                    "Please select a category",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // All fields are OK, show a success message
            // (saving to a database will be added later)
            Toast.makeText(
                this,
                "Memory details are ready to save",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}