package me.zama.cardinal.data.mediastore

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.provider.MediaStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.zama.cardinal.data.db.AppDatabase
import me.zama.cardinal.data.db.dao.AlbumDao
import me.zama.cardinal.data.db.dao.ArtistDao
import me.zama.cardinal.data.db.dao.CacheInfoDao
import me.zama.cardinal.data.db.dao.SongDao
import me.zama.cardinal.data.db.dao.get
import me.zama.cardinal.data.db.dao.put
import me.zama.cardinal.domain.entities.CacheInfo
import javax.inject.Inject


@AndroidEntryPoint
class MediaServiceImpl : Service(), MediaService {

    @Inject
    internal lateinit var appDatabase: AppDatabase

    @Inject
    internal lateinit var albumDao: AlbumDao

    @Inject
    internal lateinit var artistDao: ArtistDao

    @Inject
    internal lateinit var songDao: SongDao

    @Inject
    internal lateinit var cacheInfoDao: CacheInfoDao


    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var contentObserverThread: HandlerThread? = null
    private var contentObserver: ContentObserver? = null


    override fun onBind(intent: Intent?): IBinder = MediaService.Binder(this)


    override fun arePermissionsGranted() = checkSelfPermission(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            READ_MEDIA_AUDIO
        } else {
            READ_EXTERNAL_STORAGE
        }
    ) == PackageManager.PERMISSION_GRANTED


    override fun onReadPermissionsBecomeGranted() {
        coroutineScope.launch {
            syncWholeLibrary()
        }
        setupContentObserver()
    }

    private suspend fun getSyncStatus(): Long {
        val lastVersion = cacheInfoDao.get<String>(CacheInfo.Keys.MediaStoreVersion) ?: return -1L

        val currentVersion = MediaStore.getVersion(this@MediaServiceImpl)
        if (lastVersion != currentVersion) return -1L

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val volumes = MediaStore.getExternalVolumeNames(this@MediaServiceImpl)
            for (volume in volumes) {
                val gen = MediaStore.getGeneration(this@MediaServiceImpl, volume)
                val key = CacheInfo.Keys.MediaStoreGenerationPrefix + volume.hashCode()
                val lastGen = cacheInfoDao.get<Long>(key)
                if (gen != lastGen) return -1L
            }
            return -2L
        }

        return cacheInfoDao.get(CacheInfo.Keys.MediaStoreLastModified) ?: -1L
    }

    private suspend fun updateMediaStoreVersion(dateModified: Long) {
        cacheInfoDao.put(CacheInfo.Keys.MediaStoreVersion, MediaStore.getVersion(this))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val volumes = MediaStore.getExternalVolumeNames(this)
            for (volume in volumes) {
                val gen = MediaStore.getGeneration(this, volume)
                val key = CacheInfo.Keys.MediaStoreGenerationPrefix + volume.hashCode()
                cacheInfoDao.put(key, gen)
            }
        } else if (dateModified != 0L) {
            cacheInfoDao.put(CacheInfo.Keys.MediaStoreLastModified, dateModified)
        }
    }

    private suspend fun syncWholeLibrary() {
        var maxDateModified = 0L
        when (val syncStatus = getSyncStatus()) {
            -1L -> {
                GetAllSongs(this@MediaServiceImpl).invoke(
                    onSongInfo = {
                        maxDateModified = maxOf(maxDateModified, it.dateModified)
                        songDao.insert(it)
                    },
                    onArtist = artistDao::upsert,
                    onAlbum = albumDao::upsert
                )
            }
            -2L -> {
            }
            else -> {
                GetSongsModifierAfter(this, syncStatus).invoke(
                    onSongInfo = {
                        maxDateModified = maxOf(maxDateModified, it.dateModified)
                        songDao.insert(it)
                    },
                    onArtist = artistDao::upsert,
                    onAlbum = albumDao::upsert
                )
            }
        }
        updateMediaStoreVersion(maxDateModified)
    }

    private inner class ContentObserverImpl(handler: Handler) : ContentObserver(handler) {

        override fun onChange(selfChange: Boolean, uri: Uri?) {
            if (!arePermissionsGranted()) return

            coroutineScope.launch {
                if (uri == null) {
                    syncWholeLibrary()
                    return@launch
                }

                val id = ContentUris.parseId(uri)
                var maxDateModified = 0L

                GetSongById(this@MediaServiceImpl, id).invoke(
                    onSongInfo = {
                        maxDateModified = maxOf(maxDateModified, it.dateModified)
                        songDao.insert(it)
                    },
                    onAlbum = albumDao::upsert,
                    onArtist = artistDao::upsert
                )

                updateMediaStoreVersion(maxDateModified)
            }
        }

        override fun onChange(selfChange: Boolean) {
            onChange(selfChange, null)
        }

        override fun onChange(selfChange: Boolean, uris: MutableCollection<Uri>, flags: Int) {
            if (uris.isEmpty() || !arePermissionsGranted()) return

            coroutineScope.launch {
                val ids = uris.mapNotNull {
                    try {
                        ContentUris.parseId(it)
                    } catch (_: Exception) {
                        null
                    }
                }

                GetSongsById(this@MediaServiceImpl, ids).invoke(
                    onSongInfo = songDao::insert,
                    onAlbum = albumDao::upsert,
                    onArtist = artistDao::upsert
                )
            }
        }
    }

    private fun setupContentObserver() {
        val thread = HandlerThread("CardinalMediaServiceContentObserverThread")
        contentObserverThread = thread
        thread.start()

        val observer = ContentObserverImpl(Handler(thread.looper))
        contentObserver = observer

        contentResolver.registerContentObserver(MediaStoreQuery.CollectionUri, true, observer)
    }

    private fun tearDownContentObserver() {
        contentResolver.unregisterContentObserver(contentObserver ?: return)
        contentObserver = null

        contentObserverThread?.quitSafely()
        contentObserverThread = null
    }

    override fun onCreate() {
        super.onCreate()

        if (arePermissionsGranted()) onReadPermissionsBecomeGranted()
    }

    override fun onDestroy() {
        tearDownContentObserver()

        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        super.onDestroy()
    }
}