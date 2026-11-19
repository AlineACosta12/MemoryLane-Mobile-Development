package com.aline.memorylane.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.aline.memorylane.R

object AppColors {
    /** Green app bar behind the status bar. */
    val toolbar: Color @Composable get() = colorResource(R.color.ml_toolbar)
    val onToolbar: Color @Composable get() = colorResource(R.color.ml_on_toolbar)

    /** Terracotta hearts on cards. */
    val heart: Color @Composable get() = colorResource(R.color.ml_heart)

    /** Background of cards and form fields. */
    val card: Color @Composable get() = colorResource(R.color.ml_surface_card)
}