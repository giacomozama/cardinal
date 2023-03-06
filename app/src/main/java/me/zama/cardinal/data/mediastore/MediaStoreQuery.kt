package me.zama.cardinal.data.mediastore

import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import me.zama.cardinal.domain.entities.Album
import me.zama.cardinal.domain.entities.Artist
import me.zama.cardinal.domain.entities.CoverArt
import me.zama.cardinal.domain.entities.SongInfo

abstract class MediaStoreQuery(private val context: Context) {

    protected suspend fun query(
        onSongInfo: suspend (SongInfo) -> Unit,
        onArtist: suspend (Artist) -> Unit,
        onAlbum: suspend (Album) -> Unit,
        predicate: String?,
        vararg selectionArgs: String
    ) {
        val projection = arrayOf(
            Audio.Media._ID,
            Audio.Media.TITLE,
            Audio.Media.TRACK,
            Audio.Media.YEAR,
            Audio.Media.DURATION,
            Audio.Media.DATE_MODIFIED,
            Audio.Media.ALBUM_ID,
            Audio.Media.ALBUM,
            Audio.Media.ARTIST_ID,
            Audio.Media.ARTIST,
        )

        var selection = "${Audio.Media.IS_MUSIC} != 0 AND ${Audio.AudioColumns.TITLE} != ''"
        if (predicate != null) {
            selection += " AND ($predicate)"
        }

        return withContext(Dispatchers.IO) {
            context.contentResolver.query(
                CollectionUri,
                projection,
                selection,
                selectionArgs.takeIf { it.isNotEmpty() },
                null
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(Audio.Media.TITLE)
                val trackColumn = cursor.getColumnIndexOrThrow(Audio.Media.TRACK)
                val yearColumn = cursor.getColumnIndexOrThrow(Audio.Media.YEAR)
                val durationColumn = cursor.getColumnIndexOrThrow(Audio.Media.DURATION)
                val dateModifiedColumn = cursor.getColumnIndexOrThrow(Audio.Media.DATE_MODIFIED)
                val albumIdColumn = cursor.getColumnIndexOrThrow(Audio.Media.ALBUM_ID)
                val albumColumn = cursor.getColumnIndexOrThrow(Audio.Media.ALBUM)
                val artistIdColumn = cursor.getColumnIndexOrThrow(Audio.Media.ARTIST_ID)
                val artistColumn = cursor.getColumnIndexOrThrow(Audio.Media.ARTIST)

                val albumIds = hashSetOf<Long>()
                val artistIds = hashSetOf<Long>()

                while (isActive && cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(CollectionUri, id)

                    val genre = MediaMetadataRetriever().use { retriever ->
                        retriever.setDataSource(context, uri)
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                    }?.trim()

                    val artistId = cursor.getLong(artistIdColumn)
                    if (artistIds.add(artistId)) {
                        onArtist(
                            Artist(
                                id = artistId,
                                name = cursor.getString(artistColumn).trim()
                            )
                        )
                    }

                    val albumId = cursor.getLong(albumIdColumn)
                    val year = cursor.getInt(yearColumn)
                    if (albumIds.add(albumId)) {
                        onAlbum(
                            Album(
                                id = albumId,
                                artistId = artistId,
                                title = cursor.getString(albumColumn).trim(),
                                year = year,
                                coverArt = CoverArt.create(context, id, albumId)
                            )
                        )
                    }

                    onSongInfo(
                        SongInfo(
                            id = id,
                            title = cursor.getString(titleColumn).trim(),
                            trackNumber = cursor.getInt(trackColumn),
                            year = year,
                            duration = cursor.getLong(durationColumn),
                            dateModified = cursor.getLong(dateModifiedColumn),
                            genre = genre,
                            albumId = albumId,
                            artistId = artistId
                        )
                    )
                }
            }
        }
    }

    abstract suspend fun invoke(
        onSongInfo: suspend (SongInfo) -> Unit,
        onArtist: suspend (Artist) -> Unit,
        onAlbum: suspend (Album) -> Unit
    )

    companion object {

        val CollectionUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            Audio.Media.EXTERNAL_CONTENT_URI
        }
    }
}