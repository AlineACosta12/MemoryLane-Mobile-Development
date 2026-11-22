package com.aline.memorylane.ui.theme

import com.aline.memorylane.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

/** DM Serif Display */
val SerifFont = FontFamily(Font(R.font.dm_serif_display))

private val base = androidx.compose.material3.Typography()

/**
 * DM Serif Display for titles (app name, toolbar titles, headings, memory titles);
 * the system font (Roboto) for everything else so body text stays easy to read.
 */
val AppTypography = Typography(
    displaySmall = base.displaySmall.copy(fontFamily = SerifFont),
    headlineLarge = base.headlineLarge.copy(fontFamily = SerifFont),
    headlineMedium = base.headlineMedium.copy(fontFamily = SerifFont),
    headlineSmall = base.headlineSmall.copy(fontFamily = SerifFont),
    titleLarge = base.titleLarge.copy(fontFamily = SerifFont),
)

/** Serif section heading */
val SectionTitleStyle = TextStyle(fontFamily = SerifFont, fontSize = 20.sp, lineHeight = 26.sp)

/** Card titles */
val CardTitleStyle = TextStyle(fontFamily = SerifFont, fontSize = 17.sp, lineHeight = 22.sp)