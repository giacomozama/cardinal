package me.zama.cardinal.ui.screens.library.artists

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.ArtistWithAlbums
import me.zama.cardinal.ui.screens.album.AlbumScreenMode

val ArtistItemCollapsedHeight = 72.dp
val ArtistItemExpandedHeight = 300.dp

val ArtistItemMinCoverSize = 40.dp
val ArtistItemMaxCoverSize = 124.dp

val ArtistItemMaxCoverBoxHeight = 212.dp

@Composable
fun ArtistItem(
    modifier: Modifier = Modifier,
    albumScreenModeState: State<AlbumScreenMode>,
    isExpanded: Boolean,
    navTransition: Transition<NavBackStackEntry>,
    artistWithAlbums: ArtistWithAlbums,
    onAlbumSelected: (album: Album, size: Int, offset: IntOffset) -> Unit,
    onDismissRequest: () -> Unit,
    onClick: () -> Unit
) {
    val transition = updateTransition(
        targetState = isExpanded,
        label = "ArtistItem for ${artistWithAlbums.artist.name}"
    )

    if (transition.targetState || transition.currentState) {
        ArtistItemExpanded(
            modifier = modifier,
            albumScreenModeState = albumScreenModeState,
            navTransition = navTransition,
            artistWithAlbums = artistWithAlbums,
            transition = transition,
            onDismissRequest = onDismissRequest,
            onAlbumSelected = onAlbumSelected
        )
    } else {
        ArtistItemCollapsed(
            modifier = modifier,
            artistWithAlbums = artistWithAlbums,
            onClick = onClick
        )
    }
}
