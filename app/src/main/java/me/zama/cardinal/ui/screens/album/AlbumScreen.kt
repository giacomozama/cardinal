package me.zama.cardinal.ui.screens.album

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.zama.cardinal.domain.entities.AlbumWithArtist

val AlbumScreenMaxCoverSize = 160.dp
val HeaderMinHeight = 84.dp
val AlbumScreenHeaderPadding = 16.dp
val AlbumScreenCoverMaxCornerRadius = 24.dp
val AlbumScreenCoverMinCornerRadius = 8.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AlbumScreen(
    modifier: Modifier,
    viewModel: AlbumViewModel,
    transition: Transition<EnterExitState>
) {

    val mode by viewModel.mode.collectAsStateWithLifecycle()
    val albumWithSongs by viewModel.albumWithSongs.collectAsStateWithLifecycle()
    val artist by viewModel.artist.collectAsStateWithLifecycle()

    val density = LocalDensity.current


    val finalCoverSize = with(density) { AlbumScreenMaxCoverSize.roundToPx() }
    val headerPaddingPx = with(density) { AlbumScreenHeaderPadding.roundToPx() }
    val headerMinHeightPx = with(density) { HeaderMinHeight.toPx() }
    val headerMaxHeightPx = finalCoverSize + headerPaddingPx * 2
    val targetHeaderHeightPxState = remember { mutableStateOf(headerMaxHeightPx) }
    val headerHeightPxState = animateIntAsState(targetValue = targetHeaderHeightPxState.value)

    val fraction by remember {
        derivedStateOf {
            (headerHeightPxState.value - headerMinHeightPx) / (headerMaxHeightPx - headerMinHeightPx)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            when (mode) {
                is AlbumScreenMode.Album -> {
                    val aws = albumWithSongs
                    if (aws != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(
                                    rememberHeaderNestedScrollConnection(
                                        expandedHeaderHeightPx = headerMaxHeightPx,
                                        heightState = targetHeaderHeightPxState
                                    )
                                )
                        ) {
                            CompositionLocalProvider(
                                LocalOverscrollConfiguration provides null
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = with(density) { headerMaxHeightPx.toDp() })
                                ) {
                                    stickyHeader {
                                        AlbumScreenHeader(
                                            albumWithArtist = AlbumWithArtist(aws.album, artist!!),
                                            albumScreenMode = mode,
                                            transition = transition,
                                            headerHeightPxState = headerHeightPxState,
                                            fraction = fraction
                                        )
                                    }
                                    items(
                                        count = aws.songs.size,
                                        key = { aws.songs[it].id }
                                    ) { index ->
                                        AlbumSongItem(songInfo = aws.songs[index], index = index)
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }
                is AlbumScreenMode.Idle -> {}
                is AlbumScreenMode.Playlist -> {
                    TODO()
                }
            }
        }
        if (transition.isRunning && shouldAnimateCover(mode)) {
            AlbumScreenSharedElement(
                transition,
                mode,
                headerPaddingPx,
                headerHeightPxState,
                fraction,
                finalCoverSize
            )
        }
    }
}
