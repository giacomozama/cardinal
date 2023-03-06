package me.zama.cardinal.domain.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Relation

@Immutable
data class ArtistWithAlbumsAndSongs(
    @Embedded
    val artist: Artist,
    @Relation(
        entity = Album::class,
        parentColumn = "id",
        entityColumn = "artist_id"
    )
    val albumsWithSongs: List<AlbumWithSongs>
)
