package me.zama.cardinal.ui.screens.library.artists

import androidx.compose.animation.core.Transition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.ArtistWithAlbums
import me.zama.cardinal.ui.screens.album.AlbumScreenMode

@Composable
fun ArtistsLibraryPage(
    modifier: Modifier = Modifier,
    albumScreenModeState: State<AlbumScreenMode>,
    navTransition: Transition<NavBackStackEntry>,
    artistsWithAlbums: List<ArtistWithAlbums>,
    onAlbumSelected: (album: Album, size: Int, offset: IntOffset) -> Unit
) {
    val expandedItemArtistId = rememberSaveable { mutableStateOf(-1L) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = artistsWithAlbums,
                key = { it.artist.id }
            ) {
                ArtistItem(
                    albumScreenModeState = albumScreenModeState,
                    isExpanded = expandedItemArtistId.value == it.artist.id,
                    navTransition = navTransition,
                    artistWithAlbums = it,
                    onClick = { expandedItemArtistId.value = it.artist.id },
                    onDismissRequest = { expandedItemArtistId.value = -1 },
                    onAlbumSelected = onAlbumSelected
                )
                Divider()
            }
        }
    }
}