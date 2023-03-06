package me.zama.cardinal.ui.screens.album

import androidx.annotation.Px
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import me.zama.cardinal.domain.entities.CoverArt

sealed interface AlbumScreenMode {

    val origin: Origin

    object Idle : AlbumScreenMode {
        override val origin = Origin(0, IntOffset.Zero, 0.dp, 0.dp, CoverArt.Default)
    }

    data class Album(
        val artistId: Long,
        val albumId: Long,
        val songId: Long,
        override val origin: Origin
    ) : AlbumScreenMode

    data class Playlist(
        val playListId: Long,
        val songId: Long,
        override val origin: Origin
    ) : AlbumScreenMode

    data class Origin(
        @Px val size: Int,
        val offset: IntOffset,
        val topCornerRadius: Dp,
        val bottomCornerRadius: Dp,
        val coverArt: CoverArt
    )
}