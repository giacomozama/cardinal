package me.zama.cardinal.data.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.zama.cardinal.domain.entities.Song
import me.zama.cardinal.domain.entities.SongInfo

@Dao
interface SongDao {

    @Query("SELECT * FROM SongInfo")
    @Transaction
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT DISTINCT genre FROM SongInfo WHERE genre IS NOT NULL")
    @Transaction
    fun getAllGenres(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songInfo: SongInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songInfos: Collection<SongInfo>)

    @Delete
    suspend fun delete(songInfo: SongInfo)

    @Delete
    suspend fun delete(songInfos: Collection<SongInfo>)
}