package me.zama.cardinal.ui.screens.album

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import me.zama.cardinal.ui.screens.library.TabRowHeight

@Composable
@ReadOnlyComposable
fun shouldAnimateCover(mode: AlbumScreenMode): Boolean {
    if (mode !is AlbumScreenMode.Album) return false
    return mode.origin.offset.y >= with(LocalDensity.current) { TabRowHeight.roundToPx() }
}