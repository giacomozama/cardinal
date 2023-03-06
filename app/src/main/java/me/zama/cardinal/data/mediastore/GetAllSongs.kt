package me.zama.cardinal.data.mediastore

import android.content.Context
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.Artist
import me.zama.cardinal.domain.entities.SongInfo

class GetAllSongs(context: Context) : MediaStoreQuery(context) {

    override suspend fun invoke(
        onSongInfo: suspend (SongInfo) -> Unit,
        onArtist: suspend (Artist) -> Unit,
        onAlbum: suspend (Album) -> Unit
    ) {
        query(
            onSongInfo = onSongInfo,
            onArtist = onArtist,
            onAlbum = onAlbum,
            predicate = null
        )
    }
}