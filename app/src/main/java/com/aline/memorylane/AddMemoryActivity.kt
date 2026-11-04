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
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_memory)

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
        val dateInput = findViewById<EditText>(R.id.editMemoryDate)

        dateInput.setOnClickListener {

            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val selectedDate =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"

                    dateInput.setText(selectedDate)
                },
                year,
                month,
                day
            )

            datePicker.show()
        }

        // CATEGORY
        val categorySpinner = findViewById<Spinner>(R.id.spinnerCategory)

        val categories = arrayOf(
            "Select category",
            "Together",
            "Adventure",
            "Milestone",
            "Little Moments",
            "Celebration",
            "Special Place"
        )

        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )

        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        categorySpinner.adapter = categoryAdapter

        // SAVE
        val saveButton = findViewById<Button>(R.id.buttonSaveMemory)
        val titleInput = findViewById<EditText>(R.id.editMemoryTitle)
        val descriptionInput = findViewById<EditText>(R.id.editMemoryDescription)
        val locationButton = findViewById<Button>(R.id.buttonUseLocation)
        val locationStatus = findViewById<TextView>(R.id.textLocationStatus)

        locationButton.setOnClickListener {

            locationStatus.text = "Location integration planned for Milestone 2"

            Toast.makeText(
                this,
                "GPS will be connected in the next milestone",
                Toast.LENGTH_SHORT
            ).show()
        }

        saveButton.setOnClickListener {

            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val date = dateInput.text.toString().trim()

            if (title.isEmpty()) {
                titleInput.error = "Please enter a memory title"
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                descriptionInput.error = "Please enter a description"
                return@setOnClickListener
            }

            if (date.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please select a date",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (categorySpinner.selectedItemPosition == 0) {

                Toast.makeText(
                    this,
                    "Please select a category",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Memory details are ready to save",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}