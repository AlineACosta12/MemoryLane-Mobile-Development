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
import com.aline.memorylane.Memory
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.draw.clipToBounds
import com.aline.memorylane.ui.components.MemoryHeader

@Composable
fun AddMemoryScreen(
    onBack: () -> Unit,
    onSave: (String, String, String, String, Double?, Double?, String?) -> Unit,
    memory: Memory? = null
) {
    var title by rememberSaveable { mutableStateOf(memory?.title.orEmpty()) }
    var description by rememberSaveable { mutableStateOf(memory?.description.orEmpty()) }
    var date by rememberSaveable { mutableStateOf(memory?.date.orEmpty()) }
    var category by rememberSaveable { mutableStateOf(memory?.category.orEmpty()) }
    var savedLatitude by rememberSaveable { mutableStateOf(memory?.latitude) }
    var savedLongitude by rememberSaveable { mutableStateOf(memory?.longitude) }
    var photoUri by rememberSaveable { mutableStateOf(memory?.photoUri) }
    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            photoUri = uri.toString()
        }
    }

    var locationStatus by rememberSaveable {
        mutableStateOf("Location will be available when GPS is connected.")
    }
    var showErrors by rememberSaveable { mutableStateOf(false) }
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
    ) {


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

            // Memory date
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

            Text(
                text = "Photo",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    photoPicker.launch(arrayOf("image/*"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (photoUri == null) "Add Photo" else "Change Photo")
            }

            photoUri?.let { selectedPhoto ->
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clipToBounds()
                ) {
                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                scaleType = ImageView.ScaleType.CENTER_CROP
                                adjustViewBounds = false
                            }
                        },
                        update = { imageView ->
                            imageView.setImageURI(Uri.parse(selectedPhoto))
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                    if (showErrors && (savedLatitude == null || savedLongitude == null)) {
                        Text(
                            text = "Location is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

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
                    showErrors = true
                    if (title.isNotBlank() &&
                        description.isNotBlank() &&
                        date.isNotBlank() &&
                        category.isNotBlank() &&
                        savedLatitude != null &&
                        savedLongitude != null
                    ) {

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
