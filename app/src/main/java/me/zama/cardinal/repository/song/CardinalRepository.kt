package me.zama.cardinal.repository.song

import me.zama.cardinal.data.db.dao.AlbumDao
import me.zama.cardinal.data.db.dao.ArtistDao
import me.zama.cardinal.data.db.dao.SongDao

interface CardinalRepository : SongDao, ArtistDao, AlbumDao {
}

