package me.zama.cardinal.repository.song

import me.zama.cardinal.data.db.dao.AlbumDao
import me.zama.cardinal.data.db.dao.ArtistDao
import me.zama.cardinal.data.db.dao.SongDao
import javax.inject.Inject

class CardinalRepositoryImpl @Inject constructor(
    private val songDao: SongDao,
    private val artistDao: ArtistDao,
    private val albumDao: AlbumDao
) : CardinalRepository, SongDao by songDao, ArtistDao by artistDao, AlbumDao by albumDao