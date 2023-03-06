package me.zama.cardinal.ui.screens.library.songs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.zama.cardinal.domain.entities.CoverArtImage
import me.zama.cardinal.domain.entities.Song

val SongItemHeight = 72.dp
val SongItemCoverSize = 40.dp

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .height(SongItemHeight),
        leadingContent = {
            Box(
                modifier = Modifier.size(SongItemCoverSize)
            ) {
                CoverArtImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .size(SongItemCoverSize)
                        .clip(shape = MaterialTheme.shapes.extraSmall),
                    coverArt = song.album.coverArt,
                    size = with(LocalDensity.current) { SongItemCoverSize.roundToPx() },
                    contentDescription = null
                )
            }
        },
        headlineText = {
            Text(
                text = song.info.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingText = {
            Text(
                text = song.artist.name + " - " + song.album.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}