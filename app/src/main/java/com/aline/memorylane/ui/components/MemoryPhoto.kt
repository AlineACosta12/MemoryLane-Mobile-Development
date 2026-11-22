package com.aline.memorylane.ui.components

import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.aline.memorylane.R

// Reusable component that shows a memory's photo
// If the memory has no photo, a default landscape image is shown instead
@Composable
fun MemoryPhoto(
    photoUri: String?,
    modifier: Modifier = Modifier
) {
    // clipToBounds stops the image from drawing outside its box
    Box(modifier = modifier.clipToBounds()) {
        if (photoUri.isNullOrBlank()) {
            // No photo saved - show the default image from resources
            Image(
                painter = painterResource(R.drawable.landscape),
                contentDescription = "Memory image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Photo saved - use a normal Android ImageView inside Compose
            // to load the image from its URI
            AndroidView(
                factory = { context ->
                    // Create the ImageView once, cropped to fill the space
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                },
                update = { imageView ->
                    // Set the photo (runs again if the URI changes)
                    imageView.setImageURI(Uri.parse(photoUri))
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
