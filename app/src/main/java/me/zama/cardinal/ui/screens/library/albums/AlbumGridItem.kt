package me.zama.cardinal.ui.screens.library.albums

import androidx.compose.animation.core.Transition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.navigation.NavBackStackEntry
import me.zama.cardinal.domain.entities.AlbumWithArtist
import me.zama.cardinal.domain.entities.CoverArtImage
import me.zama.cardinal.domain.entities.backgroundColor
import me.zama.cardinal.domain.entities.bodyTextColor
import me.zama.cardinal.domain.entities.titleTextColor
import me.zama.cardinal.ui.screens.album.AlbumScreenMode
import me.zama.cardinal.ui.screens.album.shouldAnimateCover

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumGridItem(
    modifier: Modifier = Modifier,
    gridSpanCount: Int,
    albumScreenModeState: State<AlbumScreenMode>,
    transition: Transition<NavBackStackEntry>,
    albumWithArtist: AlbumWithArtist,
    onClick: (size: Int, offset: IntOffset) -> Unit
) {
    val (album, artist) = albumWithArtist
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val width = remember { (screenWidth - AlbumsGridSpacing * (1 + gridSpanCount)) / gridSpanCount }
    val height = remember { width + 120.dp }
    val density = LocalDensity.current

    val coverArtSizePx = remember { with(density) { width.roundToPx() } }

    val shouldAnimateCover = shouldAnimateCover(albumScreenModeState.value)
    val showCoverArt by remember {
        derivedStateOf {
            (albumScreenModeState.value as? AlbumScreenMode.Album)?.albumId != album.id ||
                !shouldAnimateCover ||
                !transition.isRunning
        }
    }

    var size by remember { mutableStateOf(0) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Surface(
        modifier = modifier
            .width(width)
            .height(height)
            .onGloballyPositioned {
                size = it.size.width
                offset = it.localToRoot(with(density) { Offset(x = 0f, y = -64.dp.toPx()) })
            },
        onClick = {
            onClick(coverArtSizePx, offset.round())
        },
        shape = MaterialTheme.shapes.small,
        color = albumWithArtist.album.coverArt.backgroundColor,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (showCoverArt) {
                CoverArtImage(
                    modifier = Modifier.size(width),
                    coverArt = album.coverArt,
                    size = coverArtSizePx,
                    contentDescription = null
                )
            } else {
                Spacer(modifier = Modifier.size(width))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = album.coverArt.titleTextColor
                )
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = album.coverArt.bodyTextColor
                )
            }
        }
    }
}