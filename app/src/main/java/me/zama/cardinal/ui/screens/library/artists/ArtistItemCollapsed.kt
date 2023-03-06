package me.zama.cardinal.ui.screens.library.artists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import me.zama.cardinal.domain.entities.ArtistWithAlbums
import me.zama.cardinal.domain.entities.CoverArtImage

@Composable
fun ArtistItemCollapsed(
    modifier: Modifier = Modifier,
    artistWithAlbums: ArtistWithAlbums,
    onClick: () -> Unit
) {
    val (artist, albums) = artistWithAlbums
    val minCoverSizePx = with(LocalDensity.current) { ArtistItemMinCoverSize.roundToPx() }

    ListItem(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { onClick() },
        leadingContent = {
            val shownCovers = minOf(3, albums.size)
            Box(
                modifier = Modifier
                    .width(ArtistItemMinCoverSize + 16.dp)
                    .height(ArtistItemMinCoverSize)
            ) {
                repeat(shownCovers) { index ->
                    CoverArtImage(
                        modifier = Modifier
                            .offset(x = 8.dp * index)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .zIndex(shownCovers.toFloat() - index)
                            .size(ArtistItemMinCoverSize),
                        size = minCoverSizePx,
                        coverArt = albums[index].coverArt,
                        contentDescription = null
                    )
                }
            }
        },
        headlineText = {
            Text(
                text = artist.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingText = {
            Text(
                text = "${albums.size} albums",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}