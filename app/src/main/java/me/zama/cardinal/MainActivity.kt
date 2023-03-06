package me.zama.cardinal

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import me.zama.cardinal.data.mediastore.MediaService
import me.zama.cardinal.data.mediastore.MediaServiceImpl
import me.zama.cardinal.ui.screens.main.MainScreen
import me.zama.cardinal.ui.theme.CardinalTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var mediaService: MediaService? = null
        private set

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            val svc = (service as MediaService.Binder).service
            mediaService = svc
            onServiceBound(svc)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mediaService = null
        }
    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(this, MediaServiceImpl::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                mediaService?.onReadPermissionsBecomeGranted()
            } else {
                // Permission is denied
            }
        }

    private fun onServiceBound(service: MediaService) {
        if (service.arePermissionsGranted()) {
            service.onReadPermissionsBecomeGranted()
            return
        }

        permissionRequestLauncher.launch(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CardinalTheme {
                MainScreen()
            }
        }
    }

    override fun onStop() {
        super.onStop()

        unbindService(connection)
    }
}
