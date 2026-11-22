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

@Composable
fun MemoryPhoto(
    photoUri: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clipToBounds()) {
        if (photoUri.isNullOrBlank()) {
            Image(
                painter = painterResource(R.drawable.landscape),
                contentDescription = "Memory image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                },
                update = { imageView ->
                    imageView.setImageURI(Uri.parse(photoUri))
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
