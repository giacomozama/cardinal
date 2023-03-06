package me.zama.cardinal.ui.screens.library.albums

import androidx.compose.animation.core.Transition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.AlbumWithArtist
import me.zama.cardinal.ui.screens.album.AlbumScreenMode

val AlbumsGridSpacing = 8.dp

@Composable
fun AlbumsLibraryPage(
    modifier: Modifier = Modifier,
    albumScreenModeState: State<AlbumScreenMode>,
    transition: Transition<NavBackStackEntry>,
    albumsWithArtist: List<AlbumWithArtist>,
    onAlbumSelected: (album: Album, size: Int, offset: IntOffset) -> Unit
) {
    val lazyGridState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = lazyGridState,
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(AlbumsGridSpacing),
            horizontalArrangement = Arrangement.spacedBy(AlbumsGridSpacing),
            contentPadding = PaddingValues(AlbumsGridSpacing)
        ) {
            items(
                items = albumsWithArtist,
                key = { it.album.id }
            ) { item ->
                AlbumGridItem(
                    modifier = Modifier.animateItemPlacement(),
                    gridSpanCount = 4,
                    albumScreenModeState = albumScreenModeState,
                    transition = transition,
                    albumWithArtist = item,
                    onClick = { size, offset ->
                        onAlbumSelected(item.album, size, offset)
                    }
                )
            }
        }
    }
}