package me.zama.cardinal.ui.screens.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.Transition
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.launch
import me.zama.cardinal.domain.entities.CoverArt
import me.zama.cardinal.ui.navigation.defaultNavAnimationSpec
import me.zama.cardinal.ui.screens.library.albums.AlbumsLibraryPage
import me.zama.cardinal.ui.screens.library.artists.ArtistsLibraryPage
import me.zama.cardinal.ui.screens.library.playlists.PlaylistsLibraryPage
import me.zama.cardinal.ui.screens.library.songs.SongsLibraryPage
import me.zama.cardinal.ui.screens.album.AlbumScreenMode

val TabRowHeight = 48.dp

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    albumScreenModeState: State<AlbumScreenMode>,
    transition: Transition<NavBackStackEntry>,
    onOpenPlayScreen: (AlbumScreenMode) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var currentPage by rememberSaveable { mutableStateOf(LibraryPage.Songs) }

    val sortedSongs by viewModel.songs.collectAsStateWithLifecycle()
    val artistWithAlbumList by viewModel.artistWithAlbumList.collectAsStateWithLifecycle()
    val sortedArtistsWithAlbums by viewModel.artistsWithAlbums.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = currentPage.ordinal
        ) {
            for (page in LibraryPage.values()) {
                Tab(
                    modifier = Modifier.height(TabRowHeight),
                    selected = currentPage == page,
                    onClick = {
                        coroutineScope.launch {
                            currentPage = page
                        }
                    }
                ) {
                    Text(text = stringResource(id = page.title))
                }
            }
        }
        AnimatedContent(
            targetState = currentPage,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Start,
                        animationSpec = defaultNavAnimationSpec()
                    ) with slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Start,
                        animationSpec = defaultNavAnimationSpec()
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.End,
                        animationSpec = defaultNavAnimationSpec()
                    ) with slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.End,
                        animationSpec = defaultNavAnimationSpec()
                    )
                }
            }
        ) { page ->
            when (page) {
                LibraryPage.Songs -> {
                    SongsLibraryPage(
                        songs = sortedSongs
                    )
                }
                LibraryPage.Artists -> {
                    ArtistsLibraryPage(
                        navTransition = transition,
                        albumScreenModeState = albumScreenModeState,
                        artistsWithAlbums = sortedArtistsWithAlbums,
                        onAlbumSelected = { album, size, offset ->
                            onOpenPlayScreen(
                                AlbumScreenMode.Album(
                                    artistId = album.artistId,
                                    albumId = album.id,
                                    songId = -1,
                                    origin = AlbumScreenMode.Origin(
                                        size = size,
                                        offset = offset,
                                        topCornerRadius = 12.dp,
                                        bottomCornerRadius = 0.dp,
                                        coverArt = album.coverArt ?: CoverArt.Default
                                    )
                                ),
                            )
                        }
                    )
                }
                LibraryPage.Albums -> {
                    AlbumsLibraryPage(
                        albumScreenModeState = albumScreenModeState,
                        transition = transition,
                        albumsWithArtist = artistWithAlbumList,
                        onAlbumSelected = { album, size, offset ->
                            onOpenPlayScreen(
                                AlbumScreenMode.Album(
                                    artistId = album.artistId,
                                    albumId = album.id,
                                    songId = -1,
                                    origin = AlbumScreenMode.Origin(
                                        size = size,
                                        offset = offset,
                                        topCornerRadius = 12.dp,
                                        bottomCornerRadius = 0.dp,
                                        coverArt = album.coverArt ?: CoverArt.Default
                                    )
                                ),
                            )
                        }
                    )
                }
                LibraryPage.Playlists -> {
                    PlaylistsLibraryPage()
                }
            }
        }
    }
}
