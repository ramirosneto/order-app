package br.com.order.app.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.dp

fun trashCanIcon(): ImageVector {
    return ImageVector.Builder(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        addPath(
            pathData = PathBuilder().apply {
                moveTo(3f, 6f)
                lineTo(5f, 6f)
                lineTo(21f, 6f)
                lineTo(21f, 8f)
                lineTo(19.5f, 8f)
                lineTo(17.5f, 21f)
                lineTo(6.5f, 21f)
                lineTo(4.5f, 8f)
                lineTo(3f, 8f)
                close()

                moveTo(8f, 10f)
                lineTo(10f, 10f)
                lineTo(10f, 18f)
                lineTo(8f, 18f)
                close()

                moveTo(14f, 10f)
                lineTo(16f, 10f)
                lineTo(16f, 18f)
                lineTo(14f, 18f)
                close()

                moveTo(10f, 2f)
                lineTo(14f, 2f)
                lineTo(15f, 3f)
                lineTo(19f, 3f)
                lineTo(19f, 5f)
                lineTo(5f, 5f)
                lineTo(5f, 3f)
                lineTo(9f, 3f)
                close()
            }.nodes,
            fill = SolidColor(Color.Red)
        )
    }.build()
}
