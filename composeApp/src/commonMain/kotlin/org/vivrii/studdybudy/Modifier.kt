package org.vivrii.studdybudy

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

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

@Composable
fun Modifier.measureWidth(): Modifier {
    val density = LocalDensity.current

    // TODO: case where the maximum minimum width changes due to
    //  content can we know to reset this variable back to 0.dp?
    var width by remember { mutableStateOf(0.dp) }

    return this.onGloballyPositioned { coordinates ->
        width = with(density) { max(width, coordinates.size.width.toDp()) }
    }.widthIn(width)
}
