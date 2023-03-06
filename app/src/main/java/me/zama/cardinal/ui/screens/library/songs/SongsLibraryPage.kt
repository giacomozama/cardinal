package me.zama.cardinal.ui.screens.library.songs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.zama.cardinal.domain.entities.Song

@Composable
fun SongsLibraryPage(
    modifier: Modifier = Modifier,
    songs: List<Song>
) {

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = songs,
                key = { it.info.id }
            ) { song ->
                SongItem(
                    modifier = Modifier.animateItemPlacement(),
                    song = song
                )
                Divider()
            }
        }
    }
}