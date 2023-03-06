package me.zama.cardinal.ui

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private var cached: Triple<Color, Color, ImageVector>? = null

private fun defaultCoverArt(surfaceColor: Color, onSurfaceColor: Color): ImageVector {
    cached?.let { cached ->
        if (cached.first == surfaceColor && cached.second == onSurfaceColor) {
            return cached.third
        }
    }
    val imageVector = ImageVector.Builder(
        name = "DefaultCoverArt",
        defaultWidth = 200.0.dp,
        defaultHeight = 200.0.dp,
        viewportWidth = 52.9F,
        viewportHeight = 52.9F,
    ).path(
        fill = SolidColor(onSurfaceColor),
        fillAlpha = 1.0F,
        strokeAlpha = 1.0F,
        strokeLineWidth = 0.0F,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 4.0F,
        pathFillType = PathFillType.NonZero,
    ) {
        moveTo(0.0F, 0.0F)
        horizontalLineToRelative(52.9F)
        verticalLineToRelative(52.9F)
        horizontalLineToRelative(-52.9F)
        close()
    }.path(
        fill = SolidColor(surfaceColor),
        fillAlpha = 1.0F,
        strokeAlpha = 1.0F,
        strokeLineWidth = 0.735F,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        strokeLineMiter = 4.0F,
        pathFillType = PathFillType.NonZero,
    ) {
        moveToRelative(26.5F, 9.19F)
        curveToRelative(-9.4F, 0.0F, -17.3F, 7.87F, -17.3F, 17.3F)
        curveToRelative(0.0F, 9.4F, 7.87F, 17.3F, 17.3F, 17.3F)
        reflectiveCurveToRelative(17.3F, -7.87F, 17.3F, -17.3F)
        curveToRelative(0.0F, -9.4F, -7.87F, -17.3F, -17.3F, -17.3F)

        moveTo(26.5F, 20.79F)
        curveToRelative(3.0F, 0.0F, 5.67F, 2.67F, 5.67F, 5.67F)
        curveToRelative(0.0F, 3.0F, -2.67F, 5.67F, -5.67F, 5.67F)
        curveToRelative(-3.0F, 0.0F, -5.67F, -2.68F, -5.67F, -5.67F)
        curveToRelative(0.0F, -3.0F, 2.67F, -5.67F, 5.67F, -5.67F)
        close()
    }.build()
    cached = Triple(surfaceColor, onSurfaceColor, imageVector)
    return imageVector
}

val DefaultCoverArt
    @Composable
    @ReadOnlyComposable
    get() = defaultCoverArt(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconDefaultCoverArtPreview() {
    Image(imageVector = DefaultCoverArt, contentDescription = null)
}