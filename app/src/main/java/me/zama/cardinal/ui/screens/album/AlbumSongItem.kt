package me.zama.cardinal.ui.screens.album

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.zama.cardinal.domain.entities.SongInfo
import me.zama.cardinal.utils.hmsFromMillis

val PlaySongItemHeight = 72.dp
val PlaySongItemCoverSize = 40.dp

@Composable
fun AlbumSongItem(
    index: Int,
    songInfo: SongInfo
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .height(PlaySongItemHeight),
        leadingContent = {
            Text(
                text = (index + 1).toString()
            )
        },
        headlineText = {
            Text(
                text = songInfo.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Text(
                text = hmsFromMillis(songInfo.duration)
            )
        }
    )
}