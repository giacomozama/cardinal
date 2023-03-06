package me.zama.cardinal.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.AlbumWithArtist

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album WHERE id = :id LIMIT 1")
    suspend fun getAlbumById(id: Long): Album?

    @Query("SELECT * FROM album")
    fun getAlbums(): Flow<List<Album>>

    @Upsert
    suspend fun upsert(album: Album)

    @Query("SELECT * FROM album")
    @Transaction
    fun getAlbumsWithArtist(): Flow<List<AlbumWithArtist>>
}