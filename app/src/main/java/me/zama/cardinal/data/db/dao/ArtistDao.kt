package me.zama.cardinal.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.zama.cardinal.domain.entities.Artist
import me.zama.cardinal.domain.entities.ArtistWithAlbums
import me.zama.cardinal.domain.entities.ArtistWithAlbumsAndSongs

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artist WHERE id = :id LIMIT 1")
    suspend fun getArtistById(id: Long): Artist?

    @Upsert
    suspend fun upsert(artist: Artist)

    @Query("SELECT * FROM artist")
    @Transaction
    fun getArtistsWithAlbums(): Flow<List<ArtistWithAlbums>>

    @Query("SELECT * FROM artist WHERE id = :id LIMIT 1")
    @Transaction
    suspend fun getArtistWithAlbumsAndSongs(id: Long): ArtistWithAlbumsAndSongs?
}