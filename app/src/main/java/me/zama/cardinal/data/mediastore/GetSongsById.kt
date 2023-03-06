package me.zama.cardinal.data.mediastore

import android.content.Context
import android.provider.MediaStore
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.Artist
import me.zama.cardinal.domain.entities.SongInfo

class GetSongsById(context: Context, private val ids: List<Long>) : MediaStoreQuery(context) {

    override suspend fun invoke(
        onSongInfo: suspend (SongInfo) -> Unit,
        onArtist: suspend (Artist) -> Unit,
        onAlbum: suspend (Album) -> Unit
    ) {
        query(
            onSongInfo = onSongInfo,
            onArtist = onArtist,
            onAlbum = onAlbum,
            predicate = "${MediaStore.Audio.Media._ID} IN (${"?, ".repeat(ids.size).dropLast(2)})",
            selectionArgs = Array(ids.size) { ids[it].toString() }
        )
    }
}