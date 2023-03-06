package me.zama.cardinal.domain.entities

import androidx.room.Embedded
import androidx.room.Relation

data class Song(
    @Embedded
    val info: SongInfo,
    @Relation(
        parentColumn = "artist_id",
        entityColumn = "id"
    )
    val artist: Artist,
    @Relation(
        parentColumn = "album_id",
        entityColumn = "id"
    )
    val album: Album
)
