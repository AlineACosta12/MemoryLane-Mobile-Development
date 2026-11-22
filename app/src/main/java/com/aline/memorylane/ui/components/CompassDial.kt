package com.aline.memorylane.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aline.memorylane.ui.theme.MemoryLaneTheme

// Draws the compass dial (circle, tick marks and N/E/S/W letters) using a Canvas
// Used on the Memory Compass screen
@Composable
fun CompassDial(
    modifier: Modifier = Modifier
) {
    // Colours taken from the app theme
    val faceColor = MaterialTheme.colorScheme.surface
    val ringColor = MaterialTheme.colorScheme.outline
    val markColor = MaterialTheme.colorScheme.onSurfaceVariant
    val northColor = MaterialTheme.colorScheme.primary

    // The four main directions, in clockwise order
    val letters = listOf("N", "E", "S", "W")

    // Style for E, S and W
    val letterStyle = TextStyle(
        color = markColor,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    // North is bigger and a different colour so it stands out
    val northStyle = MaterialTheme.typography.headlineMedium.copy(
        color = northColor,
        fontSize = 22.sp
    )

    // Needed to measure text size so we can centre the letters
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {

        // Centre of the canvas and the size of the circle
        // (2dp smaller so the ring doesn't get cut off at the edge)
        val center = this.center
        val radius = size.minDimension / 2f - 2.dp.toPx()

        // Compass face
        drawCircle(
            color = faceColor,
            radius = radius,
            center = center
        )

        // Outer ring
        drawCircle(
            color = ringColor,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Compass tick marks
        // Where the tick marks start (a bit inside the outer ring)
        val outer = radius - 10.dp.toPx()

        // Draw a tick every 5 degrees
        for (degrees in 0 until 360 step 5) {

            // Skip 0, 90, 180, 270 because the letters go there
            if (degrees % 90 == 0) continue

            // Every 30 degrees is a bigger tick
            val major = degrees % 30 == 0

            // Big ticks are longer than small ticks
            val inner = outer -
                    (if (major) 14 else 7).dp.toPx()

            // Rotate the canvas and draw a line at the top,
            // so each tick ends up at the right angle
            rotate(
                degrees = degrees.toFloat(),
                pivot = center
            ) {
                drawLine(
                    // Small ticks are lighter (more see-through)
                    color = if (major) {
                        markColor
                    } else {
                        markColor.copy(alpha = 0.43f)
                    },
                    start = Offset(
                        center.x,
                        center.y - outer
                    ),
                    end = Offset(
                        center.x,
                        center.y - inner
                    ),
                    // Big ticks are thicker
                    strokeWidth = (
                            if (major) 3f else 1.5f
                            ).dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // Cardinal directions

        letters.forEachIndexed { index, letter ->

            // Measure the letter (index 0 is North, so it uses the north style)
            val layout = textMeasurer.measure(
                text = letter,
                style = if (index == 0) {
                    northStyle
                } else {
                    letterStyle
                }
            )

            // How far from the centre the letters are placed
            val distance = radius - 34.dp.toPx()

            // Position of each letter around the circle
            val position = when (index) {
                0 -> Offset(center.x, center.y - distance) // North
                1 -> Offset(center.x + distance, center.y) // East
                2 -> Offset(center.x, center.y + distance) // South
                else -> Offset(center.x - distance, center.y) // West
            }

            // Draw the letter centred on its position
            // (subtract half the width and height so it isn't drawn from the corner)
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(
                    position.x - layout.size.width / 2f,
                    position.y - layout.size.height / 2f
                )
            )
        }
    }
}

// Preview of the dial in Android Studio
@Preview(showBackground = true)
@Composable
private fun CompassDialPreview() {
    MemoryLaneTheme {
        Box(
            modifier = Modifier.size(300.dp)
        ) {
            CompassDial(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
