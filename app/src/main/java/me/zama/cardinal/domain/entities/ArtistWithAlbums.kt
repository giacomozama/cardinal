package me.zama.cardinal.domain.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Relation

@Immutable
data class ArtistWithAlbums(
    @Embedded
    val artist: Artist,
    @Relation(
        parentColumn = "id",
        entityColumn = "artist_id"
    )
    val albums: List<Album>
)
