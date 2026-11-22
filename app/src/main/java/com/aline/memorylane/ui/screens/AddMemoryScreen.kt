package com.aline.memorylane.ui.screens

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aline.memorylane.Memory
import com.aline.memorylane.sensors.location.LocationHelper
import com.aline.memorylane.ui.components.MemoryHeader
import com.aline.memorylane.ui.components.MemoryPhoto
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import java.util.Calendar
import java.util.Locale

// Screen for adding a new memory or editing an existing one
// If a memory is passed in, the fields are filled with its data (edit mode)
@Composable
fun AddMemoryScreen(
    onBack: () -> Unit,
    onSave: (String, String, String, String, Double?, Double?, String?) -> Unit,
    memory: Memory? = null
) {
    // Form fields - rememberSaveable keeps the values when the screen rotates
    var title by rememberSaveable { mutableStateOf(memory?.title.orEmpty()) }
    var description by rememberSaveable { mutableStateOf(memory?.description.orEmpty()) }
    var date by rememberSaveable { mutableStateOf(memory?.date.orEmpty()) }
    var category by rememberSaveable { mutableStateOf(memory?.category.orEmpty()) }
    var savedLatitude by rememberSaveable { mutableStateOf(memory?.latitude) }
    var savedLongitude by rememberSaveable { mutableStateOf(memory?.longitude) }
    var photoUri by rememberSaveable { mutableStateOf(memory?.photoUri) }
    val context = LocalContext.current

    // Opens the phone's file picker so the user can choose a photo
    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            // Keep permission to read the photo after the app restarts
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            photoUri = uri.toString()
        }
    }

    // Message shown in the location box
    var locationStatus by rememberSaveable {
        mutableStateOf("Location will be available when GPS is connected.")
    }
    // Only show error messages after the user presses Save
    var showErrors by rememberSaveable { mutableStateOf(false) }
    // Controls if the category dropdown is open or closed
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    // Asks the user for location permission and gets the location if allowed
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        // Check if precise location was allowed
        val preciseGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        // Check if approximate location was allowed
        val approximateGranted =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (preciseGranted || approximateGranted) {

            locationStatus = "Getting current location..."

            // Get the current location using our helper class
            LocationHelper.getCurrentLocation(context) { location ->

                locationStatus = if (location != null) {
                    // Save the coordinates and show them on screen
                    savedLatitude = location.latitude
                    savedLongitude = location.longitude
                    String.format(
                        Locale.US,
                        "Latitude: %.6f\nLongitude: %.6f",
                        location.latitude,
                        location.longitude
                    )
                } else {
                    "Could not get location. Check GPS and try again."
                }
            }

        } else {
            // User said no to the permission
            locationStatus = "Location permission denied."
        }
    }

    // Main layout - scrollable so everything fits on small screens
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {


        // Top bar - title changes depending on add or edit mode
        MemoryHeader(
            title = if (memory == null) "Add Memory" else "Edit Memory",
            onBack = onBack
        )

        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Save a place, keep its story.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Memory title
            OutlinedTextField(
                value = title,
                isError = showErrors && title.isBlank(),
                supportingText = {
                    if (showErrors && title.isBlank()) {
                        Text("Title is required")
                    }
                },
                onValueChange = { title = it },
                label = { Text("Title") },
                placeholder = { Text("e.g. First Coffee Together") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Memory description
            OutlinedTextField(
                value = description,
                isError = showErrors && description.isBlank(),
                supportingText = {
                    if (showErrors && description.isBlank()) {
                        Text("Description is required")
                    }
                },
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("Tell the story of this memory...") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Memory date - read only, the user picks it with the date picker
            OutlinedTextField(
                value = date,
                isError = showErrors && date.isBlank(),
                supportingText = {
                    if (showErrors && date.isBlank()) {
                        Text("Date is required")
                    }
                },
                onValueChange = {},
                label = { Text("Date") },
                placeholder = { Text("Select a date") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    TextButton(
                        onClick = {
                            // Start the date picker on today's date
                            val today = Calendar.getInstance()

                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    // Save date as dd/MM/yyyy (month starts at 0, so +1)
                                    date = "%02d/%02d/%04d".format(
                                        day,
                                        month + 1,
                                        year
                                    )
                                },
                                today.get(Calendar.YEAR),
                                today.get(Calendar.MONTH),
                                today.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    ) {
                        Text("Select")
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Memory category - read only field with a dropdown menu
            Box(modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = category,
                    isError = showErrors && category.isBlank(),
                    supportingText = {
                        if (showErrors && category.isBlank()) {
                            Text("Category is required")
                        }
                    },
                    onValueChange = {},
                    label = { Text("Category") },
                    placeholder = { Text("Choose a category") },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        // Opens the dropdown
                        TextButton(
                            onClick = { categoryMenuExpanded = true }
                        ) {
                            Text("Choose")
                        }
                    }
                )

                // List of categories the user can pick from
                DropdownMenu(
                    expanded = categoryMenuExpanded,
                    onDismissRequest = {
                        categoryMenuExpanded = false
                    }
                ) {
                    listOf(
                        "Together",
                        "Adventure",
                        "Milestone",
                        "Little Moments",
                        "Celebration",
                        "Special Place"
                    ).forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                // Save the chosen category and close the menu
                                category = option
                                categoryMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Photo section
            Text(
                text = "Photo",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Button to pick a photo (only image files)
            OutlinedButton(
                onClick = {
                    photoPicker.launch(arrayOf("image/*"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (photoUri == null) "Add Photo" else "Change Photo")
            }

            // Show a preview of the photo if one was chosen
            photoUri?.let { selectedPhoto ->
                Spacer(modifier = Modifier.height(12.dp))

                MemoryPhoto(
                    photoUri = selectedPhoto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Location section
            Text(
                text = "Location",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Card that shows the location info
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = if (savedLatitude != null) "Location added" else "No location added",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Shows the coordinates or a status message
                    Text(
                        text = locationStatus,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // Error if the user tries to save without a location
                    if (showErrors && (savedLatitude == null || savedLongitude == null)) {
                        Text(
                            text = "Location is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Use Location button - asks for permission first
                    OutlinedButton(
                        onClick = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Use Location")
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Save memory button
            Button(
                onClick = {
                    // Turn on error messages for any empty fields
                    showErrors = true
                    // Only save if all required fields are filled in
                    if (title.isNotBlank() &&
                        description.isNotBlank() &&
                        date.isNotBlank() &&
                        category.isNotBlank() &&
                        savedLatitude != null &&
                        savedLongitude != null
                    ) {

                        // Send the data back (photo is optional)
                        onSave(
                            title.trim(),
                            description.trim(),
                            date,
                            category,
                            savedLatitude,
                            savedLongitude,
                            photoUri
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save Memory")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Preview so we can see the screen in Android Studio
@Preview(showBackground = true)
@Composable
private fun AddMemoryScreenPreview() {
    MemoryLaneTheme {

        AddMemoryScreen(
            onBack = {},
            onSave = { _, _, _, _, _, _, _ -> }
        )
    }
}
