package me.zama.cardinal.ui.screens.library.artists

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.zIndex
import androidx.navigation.NavBackStackEntry
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.ArtistWithAlbums
import me.zama.cardinal.domain.entities.CoverArtImage
import me.zama.cardinal.domain.entities.backgroundColor
import me.zama.cardinal.ui.screens.album.AlbumScreenMode
import me.zama.cardinal.ui.screens.album.shouldAnimateCover

val ArtistItemAnimationDuration = 600

val FirstHalfFastOutSlowInEasing = Easing {
    when {
        it < .5f -> FastOutSlowInEasing.transform(it * 2)
        else -> 1f
    }
}

val SecondHalfFastOutSlowInEasing = Easing {
    when {
        it < .5f -> 0f
        else -> FastOutSlowInEasing.transform((it - .5f) * 2)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistItemExpanded(
    modifier: Modifier = Modifier,
    albumScreenModeState: State<AlbumScreenMode>,
    navTransition: Transition<NavBackStackEntry>,
    artistWithAlbums: ArtistWithAlbums,
    transition: Transition<Boolean>,
    onAlbumSelected: (album: Album, size: Int, offset: IntOffset) -> Unit,
    onDismissRequest: () -> Unit
) {
    val (artist, albums) = artistWithAlbums
    val density = LocalDensity.current

    BackHandler {
        onDismissRequest()
    }

    val minCoverSizePx = with(density) { ArtistItemMinCoverSize.roundToPx() }
    val maxCoverSizePx = with(density) { ArtistItemMaxCoverSize.roundToPx() }

    fun <T> Transition.Segment<Boolean>.animationSpec(
        easing: Easing = if (targetState) {
            SecondHalfFastOutSlowInEasing
        } else {
            FirstHalfFastOutSlowInEasing
        }
    ) = tween<T>(
        durationMillis = ArtistItemAnimationDuration,
        easing = easing
    )

    val height by transition.animateDp(
        label = "height",
        transitionSpec = {
            animationSpec(if (targetState) FirstHalfFastOutSlowInEasing else SecondHalfFastOutSlowInEasing)
        }
    ) {
        if (it) ArtistItemExpandedHeight else ArtistItemCollapsedHeight
    }

    val coversBoxHeight by transition.animateDp(
        label = "coversBoxHeight",
        transitionSpec = { animationSpec() }
    ) {
        if (it) ArtistItemMaxCoverBoxHeight else ArtistItemMinCoverSize
    }

    val coversSize by transition.animateDp(
        label = "coversSize",
        transitionSpec = { animationSpec() }
    ) {
        if (it) ArtistItemMaxCoverSize else ArtistItemMinCoverSize
    }

    val coversCornerRadius by transition.animateDp(
        label = "coversCornerRadius",
        transitionSpec = { animationSpec() }
    ) {
        if (it) 16.dp else 8.dp
    }

    val coversYOffset by transition.animateDp(
        label = "coversYOffset",
        transitionSpec = {
            animationSpec(if (targetState) FastOutSlowInEasing else SecondHalfFastOutSlowInEasing)
        }
    ) {
        if (it) 72.dp else 16.dp
    }

    val textXOffset by transition.animateDp(
        label = "textXOffset",
        transitionSpec = { animationSpec() }
    ) {
        if (it) 16.dp else 88.dp
    }

    val coversAlpha by transition.animateFloat(
        label = "coversAlpha",
        transitionSpec = { animationSpec() }
    ) {
        if (it) 1f else 0f
    }

    val coversStartOffset by transition.animateDp(
        label = "coversStartOffset",
        transitionSpec = { animationSpec() }
    ) {
        if (it) 0.dp else (-40).dp
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        LazyRow(
            modifier = Modifier
                .offset(y = coversYOffset)
                .height(coversBoxHeight),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = albums.size) { index ->
                val coverArt = albums[index].coverArt
                var offset by remember { mutableStateOf(IntOffset.Zero) }

                val shouldAnimateCover = shouldAnimateCover(albumScreenModeState.value)
                val showCoverArt by remember {
                    derivedStateOf {
                        (albumScreenModeState.value as? AlbumScreenMode.Album)?.albumId != albums[index].id ||
                            !shouldAnimateCover ||
                            !navTransition.isRunning
                    }
                }

                Surface(
                    modifier = Modifier
                        .offset(x = coversStartOffset * index)
                        .zIndex(albums.size.toFloat() - index)
                        .width(coversSize)
                        .height(coversBoxHeight)
                        .alpha(if (index < 3) 1f else coversAlpha),
                    color = coverArt.backgroundColor,
                    shape = RoundedCornerShape(coversCornerRadius),
                    onClick = {
                        onAlbumSelected(albums[index], maxCoverSizePx, offset)
                    }
                ) {
                    Column {
                        CoverArtImage(
                            modifier = Modifier
                                .size(coversSize)
                                .onGloballyPositioned { coords ->
                                    offset = coords
                                        .localToRoot(Offset(x = 0f, y = with(density) { -64.dp.toPx() }))
                                        .round()
                                }
                                .alpha(if (showCoverArt) 1f else 0f),
                            size = maxCoverSizePx,
                            thumbnailSize = minCoverSizePx,
                            coverArt = coverArt,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .requiredWidth(ArtistItemMaxCoverSize - 32.dp)
                                .requiredHeight(ArtistItemMaxCoverBoxHeight - ArtistItemMaxCoverSize - 32.dp)
                                .alpha(coversAlpha),
                            text = albums[index].title,
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .offset(x = textXOffset)
                .height(ArtistItemCollapsedHeight),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = artist.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${albums.size} albums",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}