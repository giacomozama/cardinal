package me.zama.cardinal.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.zama.cardinal.data.db.dao.AlbumDao
import me.zama.cardinal.data.db.dao.ArtistDao
import me.zama.cardinal.data.db.dao.CacheInfoDao
import me.zama.cardinal.data.db.dao.SongDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideAlbumDao(appDatabase: AppDatabase): AlbumDao = appDatabase.albumDao()

    @Provides
    fun provideArtistDao(appDatabase: AppDatabase): ArtistDao = appDatabase.artistDao()

    @Provides
    fun provideCacheInfoDao(appDatabase: AppDatabase): CacheInfoDao = appDatabase.cacheInfoDao()

    @Provides
    fun provideSongDao(appDatabase: AppDatabase): SongDao = appDatabase.songDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "music_player").build()
}