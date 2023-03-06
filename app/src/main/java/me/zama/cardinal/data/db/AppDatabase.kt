package me.zama.cardinal.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.zama.cardinal.data.db.dao.AlbumDao
import me.zama.cardinal.data.db.dao.ArtistDao
import me.zama.cardinal.data.db.dao.CacheInfoDao
import me.zama.cardinal.data.db.dao.SongDao
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.Artist
import me.zama.cardinal.domain.entities.CacheInfo
import me.zama.cardinal.domain.entities.SongInfo

@Database(
    entities = [
        SongInfo::class,
        CacheInfo::class,
        Artist::class,
        Album::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    abstract fun cacheInfoDao(): CacheInfoDao

    abstract fun artistDao(): ArtistDao

    abstract fun albumDao(): AlbumDao
}
