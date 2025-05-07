package org.vivrii.studdybudy

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.debug(
    color: Color = Color.Red,
    width: Dp = 1.dp,
    spacing: Dp = 6.dp
): Modifier {
    return this.border(width = width, color = color).drawBehind {
        val stripePx = width.toPx()
        val spacingPx = spacing.toPx()

        var x = -size.height
        while (x < size.width) {
            val start = if (x < 0.0f) {
                Offset(0.0f, size.height + x)
            } else {
                Offset(x, size.height)
            }
            val end = if (x + size.height < size.width) {
                Offset(x + size.height, 0.0f)
            } else {
                Offset(size.width, size.height - size.width + x)
            }
            drawLine(color = color, start = start, end = end, strokeWidth = stripePx)
            x += spacingPx
        }
    }
}