package com.aline.memorylane.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.aline.memorylane.R

/** Rounded corners: 12dp for buttons and fields, 16dp for cards, 24dp for large surfaces. */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
)

@Composable
fun MemoryLaneTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val base = if (dark) darkColorScheme() else lightColorScheme()

    val card = colorResource(R.color.ml_surface_card)
    val surfaceVariant = colorResource(R.color.ml_surface_variant)
    val outline = colorResource(R.color.ml_outline)

    val colors = base.copy(
        primary = colorResource(R.color.ml_primary),
        onPrimary = colorResource(R.color.ml_on_primary),
        primaryContainer = colorResource(R.color.ml_primary_container),
        onPrimaryContainer = colorResource(R.color.ml_on_primary_container),
        secondary = colorResource(R.color.ml_secondary),
        onSecondary = colorResource(R.color.ml_on_secondary),
        secondaryContainer = colorResource(R.color.ml_secondary_container),
        onSecondaryContainer = colorResource(R.color.ml_on_secondary_container),
        tertiary = colorResource(R.color.ml_tertiary),
        tertiaryContainer = colorResource(R.color.ml_tertiary_container),
        onTertiaryContainer = colorResource(R.color.ml_on_tertiary_container),
        background = colorResource(R.color.ml_background),
        onBackground = colorResource(R.color.ml_on_surface),
        surface = colorResource(R.color.ml_surface),
        onSurface = colorResource(R.color.ml_on_surface),
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = colorResource(R.color.ml_on_surface_variant),
        surfaceContainerLowest = card,
        surfaceContainerLow = card,
        surfaceContainer = surfaceVariant,
        surfaceContainerHigh = card,
        surfaceContainerHighest = surfaceVariant,
        outline = outline,
        outlineVariant = outline,
        error = colorResource(R.color.ml_error),
    )

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}
