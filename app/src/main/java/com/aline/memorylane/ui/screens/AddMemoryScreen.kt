package com.aline.memorylane.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.aline.memorylane.ui.theme.MemoryLaneTheme
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.aline.memorylane.sensors.location.LocationHelper
import java.util.Locale


@Composable
fun AddMemoryScreen(
    onBack: () -> Unit,
    onSave: (String, String, String, String, Double?, Double?) -> Unit
) {

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var savedLatitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var savedLongitude by rememberSaveable { mutableStateOf<Double?>(null) }
    val context = LocalContext.current
    var locationStatus by rememberSaveable {
        mutableStateOf("Location will be available when GPS is connected.")
    }
    var category by rememberSaveable { mutableStateOf("") }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val preciseGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        val approximateGranted =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (preciseGranted || approximateGranted) {

            locationStatus = "Getting current location..."

            LocationHelper.getCurrentLocation(context) { location ->

                locationStatus = if (location != null) {
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
            locationStatus = "Location permission denied."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        // Back button
        TextButton(onClick = onBack) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Screen heading
        Text(
            text = "Add Memory",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Save a place, keep its story.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Memory title
        OutlinedTextField(
            value = title,
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
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Tell the story of this memory...") },
            minLines = 4,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Memory date
        OutlinedTextField(
            value = date,
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
                        val today = Calendar.getInstance()

                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
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

        // Memory category
        Box(modifier = Modifier.fillMaxWidth()) {

            OutlinedTextField(
                value = category,
                onValueChange = {},
                label = { Text("Category") },
                placeholder = { Text("Choose a category") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    TextButton(
                        onClick = { categoryMenuExpanded = true }
                    ) {
                        Text("Choose")
                    }
                }
            )

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
                            category = option
                            categoryMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Location section
        Text(
            text = "Location",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

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

                Text(
                    text = locationStatus,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Use Location button
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
                onSave(
                    title.trim(),
                    description.trim(),
                    date,
                    category,
                    savedLatitude,
                    savedLongitude
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = title.isNotBlank() &&
                    description.isNotBlank() &&
                    date.isNotBlank() &&
                    category.isNotBlank(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Save Memory")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMemoryScreenPreview() {
    MemoryLaneTheme {

        AddMemoryScreen(
            onBack = {},
            onSave = { _, _, _, _, _, _ -> }
        )
    }
}
