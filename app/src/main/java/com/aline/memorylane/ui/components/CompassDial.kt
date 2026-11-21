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

@Composable
fun CompassDial(
    modifier: Modifier = Modifier
) {
    val faceColor = MaterialTheme.colorScheme.surface
    val ringColor = MaterialTheme.colorScheme.outline
    val markColor = MaterialTheme.colorScheme.onSurfaceVariant
    val northColor = MaterialTheme.colorScheme.primary

    val letters = listOf("N", "E", "S", "W")

    val letterStyle = TextStyle(
        color = markColor,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    val northStyle = MaterialTheme.typography.headlineMedium.copy(
        color = northColor,
        fontSize = 22.sp
    )

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {

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
        val outer = radius - 10.dp.toPx()

        for (degrees in 0 until 360 step 5) {

            if (degrees % 90 == 0) continue

            val major = degrees % 30 == 0

            val inner = outer -
                    (if (major) 14 else 7).dp.toPx()

            rotate(
                degrees = degrees.toFloat(),
                pivot = center
            ) {
                drawLine(
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
                    strokeWidth = (
                            if (major) 3f else 1.5f
                            ).dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // Cardinal directions

        letters.forEachIndexed { index, letter ->

            val layout = textMeasurer.measure(
                text = letter,
                style = if (index == 0) {
                    northStyle
                } else {
                    letterStyle
                }
            )

            val distance = radius - 34.dp.toPx()

            val position = when (index) {
                0 -> Offset(center.x, center.y - distance) // North
                1 -> Offset(center.x + distance, center.y) // East
                2 -> Offset(center.x, center.y + distance) // South
                else -> Offset(center.x - distance, center.y) // West
            }

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
