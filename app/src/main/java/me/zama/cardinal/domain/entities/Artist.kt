package me.zama.cardinal.domain.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity
data class Artist(
    @PrimaryKey
    val id: Long,
    val name: String
)
