package me.zama.cardinal.ui.screens.play

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import me.zama.cardinal.domain.entities.Song
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor() : ViewModel() {

    val currentSong: StateFlow<Song> = TODO()
}