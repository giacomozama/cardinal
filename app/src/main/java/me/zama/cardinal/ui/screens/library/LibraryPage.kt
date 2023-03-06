package me.zama.cardinal.ui.screens.library

import androidx.annotation.StringRes
import me.zama.cardinal.R

enum class LibraryPage(@StringRes val title: Int) {
    Songs(R.string.songs),
    Artists(R.string.artists),
    Albums(R.string.albums),
    Playlists(R.string.playlists)
}