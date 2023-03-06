package me.zama.cardinal.ui.screens.album

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.Transition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import me.zama.cardinal.domain.entities.AlbumWithArtist
import me.zama.cardinal.domain.entities.CoverArtImage
import me.zama.cardinal.domain.entities.backgroundColor
import me.zama.cardinal.domain.entities.bodyTextColor
import me.zama.cardinal.domain.entities.titleTextColor

@Composable
fun AlbumScreenHeader(
    albumWithArtist: AlbumWithArtist,
    albumScreenMode: AlbumScreenMode,
    transition: Transition<EnterExitState>,
    fraction: Float,
    headerHeightPxState: State<Int>
) {
    val (album, artist) = albumWithArtist
    val density = LocalDensity.current
    val coverCornerRadius = lerp(AlbumScreenCoverMinCornerRadius, AlbumScreenCoverMaxCornerRadius, fraction)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(density) { headerHeightPxState.value.toDp() }),
        color = album.coverArt.backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(AlbumScreenHeaderPadding)
                .fillMaxSize()
        ) {
            val isVisible = !transition.isRunning || !shouldAnimateCover(mode = albumScreenMode)
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .alpha(if (isVisible) 1f else 0f),
                shape = RoundedCornerShape(coverCornerRadius),
                color = Color.Transparent
            ) {
                CoverArtImage(
                    modifier = Modifier.fillMaxSize(),
                    coverArt = album.coverArt,
                    size = with(density) { AlbumScreenMaxCoverSize.roundToPx() },
                    contentDescription = "Album cover for ${album.title}"
                )
            }
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = album.title,
                    color = album.coverArt.titleTextColor,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 1,
                    maxLines = if (fraction > .5) 2 else 1,
                    lineHeight = lerp(
                        MaterialTheme.typography.titleMedium.lineHeight,
                        MaterialTheme.typography.headlineMedium.lineHeight,
                        fraction
                    ),
                    fontSize = lerp(
                        MaterialTheme.typography.titleMedium.fontSize,
                        MaterialTheme.typography.headlineMedium.fontSize,
                        fraction
                    )
                )
                Text(
                    text = artist.name,
                    color = album.coverArt.bodyTextColor,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 1,
                    maxLines = if (fraction > .5) 2 else 1,
                    lineHeight = lerp(
                        start = MaterialTheme.typography.titleSmall.lineHeight,
                        stop = MaterialTheme.typography.headlineSmall.lineHeight,
                        fraction = fraction
                    ),
                    fontSize = lerp(
                        start = MaterialTheme.typography.titleSmall.fontSize,
                        stop = MaterialTheme.typography.headlineSmall.fontSize,
                        fraction = fraction
                    )
                )
            }
        }
    }
}