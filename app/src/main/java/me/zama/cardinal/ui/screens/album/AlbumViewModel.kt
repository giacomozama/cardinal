package me.zama.cardinal.ui.screens.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.zama.cardinal.repository.song.usecase.GetArtistWithAlbumsAndSongsUseCase
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    getArtistWithAlbumsAndSongsUseCase: GetArtistWithAlbumsAndSongsUseCase
) : ViewModel() {

    private val _mode = MutableStateFlow<AlbumScreenMode>(AlbumScreenMode.Idle)
    val mode: StateFlow<AlbumScreenMode> = _mode

    fun setMode(albumScreenMode: AlbumScreenMode) {
        _mode.value = albumScreenMode
    }

    private val artistWithAlbumsAndSongs = mode.map { mode ->
        if (mode !is AlbumScreenMode.Album) return@map null
        getArtistWithAlbumsAndSongsUseCase.invoke(mode.artistId)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val artist = artistWithAlbumsAndSongs.map { it?.artist }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val albumWithSongs = combine(artistWithAlbumsAndSongs, mode) { aas, mode ->
        if (aas == null || mode !is AlbumScreenMode.Album) return@combine null
        val albumWithSongs = aas.albumsWithSongs.find { it.album.id == mode.albumId } ?: return@combine null
        albumWithSongs.copy(songs = albumWithSongs.songs.sortedBy { it.trackNumber })
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

}
