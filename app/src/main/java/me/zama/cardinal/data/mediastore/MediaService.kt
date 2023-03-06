package me.zama.cardinal.data.mediastore

interface MediaService {

    class Binder(val service: MediaService) : android.os.Binder()

    fun onReadPermissionsBecomeGranted()

    fun arePermissionsGranted(): Boolean
}