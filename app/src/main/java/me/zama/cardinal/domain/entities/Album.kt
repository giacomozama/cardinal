package me.zama.cardinal.domain.entities

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Immutable
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Artist::class,
            parentColumns = ["id"],
            childColumns = ["artist_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class Album(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "artist_id", index = true)
    val artistId: Long,
    val title: String,
    val year: Int,
    @Embedded
    val coverArt: CoverArt?
)