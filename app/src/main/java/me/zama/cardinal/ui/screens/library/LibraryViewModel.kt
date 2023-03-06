package me.zama.cardinal.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.zama.cardinal.repository.song.usecase.GetAlbumsWithArtist
import me.zama.cardinal.repository.song.usecase.GetArtistsWithAlbumsUseCase
import me.zama.cardinal.repository.song.usecase.GetGenresUseCase
import me.zama.cardinal.repository.song.usecase.GetSongsUseCase
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    getSongsUseCase: GetSongsUseCase,
    getAlbumsWithArtist: GetAlbumsWithArtist,
    getArtistsWithAlbumsUseCase: GetArtistsWithAlbumsUseCase,
    getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    val songs = getSongsUseCase.invoke().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val artistWithAlbumList = getAlbumsWithArtist.invoke()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val artistsWithAlbums = getArtistsWithAlbumsUseCase.invoke()
        .map { awa -> awa.filter { it.albums.isNotEmpty() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}