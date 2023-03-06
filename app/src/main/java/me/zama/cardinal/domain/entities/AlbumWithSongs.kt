package me.zama.cardinal.domain.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Relation

@Immutable
data class AlbumWithSongs(
    @Embedded
    val album: Album,
    @Relation(
        parentColumn = "id",
        entityColumn = "album_id"
    )
    val songs: List<SongInfo>
)