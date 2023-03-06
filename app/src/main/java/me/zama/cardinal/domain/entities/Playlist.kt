package me.zama.cardinal.domain.entities

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.ui.graphics.vector.ImageVector
import me.zama.cardinal.R

sealed class Playlist(
    val id: Long,
    val name: Name,
    val icon: ImageVector
) {
    abstract val songs: List<Long>

    class UserCreated(
        id: Long = -1,
        name: String,
        override val songs: List<Long>
    ) : Playlist(id, Name.Custom(name), Icons.Filled.PlaylistPlay)

    sealed class AutoGenerated(
        id: Long = -1,
        @StringRes name: Int,
        icon: ImageVector
    ) : Playlist(id, Name.Resource(name), icon) {

        abstract fun update()

        class Hot : AutoGenerated(
            id = -3,
            name = R.string.top_tracks,
            icon = Icons.Filled.LocalFireDepartment
        ) {

            override fun update() {
                TODO("Not yet implemented")
            }

            override val songs: List<Long>
                get() = TODO("Not yet implemented")
        }

        class LastAdded : AutoGenerated(
            id = -1,
            name = R.string.last_added,
            icon = Icons.Filled.NewReleases
        ) {
            override fun update() {
                TODO("Not yet implemented")
            }

            override val songs: List<Long>
                get() = TODO("Not yet implemented")
        }

        class LastPlayed : AutoGenerated(
            id = -2,
            name = R.string.last_played,
            icon = Icons.Filled.History
        ) {

            override fun update() {
                TODO("Not yet implemented")
            }

            override val songs: List<Long>
                get() = TODO("Not yet implemented")

        }

        class ShuffleAll : AutoGenerated(
            id = -4,
            name = R.string.shuffle_all,
            icon = Icons.Filled.Shuffle
        ) {

            override fun update() {
                TODO("Not yet implemented")
            }

            override val songs: List<Long>
                get() = TODO("Not yet implemented")
        }
    }

    sealed interface Name {

        class Resource(@StringRes val id: Int) : Name

        class Custom(val value: String) : Name
    }
}