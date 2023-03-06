package me.zama.cardinal.ui.navigation

sealed class Screen(val route: String) {

    object Play : Screen(Routes.Play)

    object Album : Screen(Routes.Album) {
        fun formatRoute(albumId: Long) = Routes.Album.replace("{albumId}", albumId.toString())
    }

    object Artist : Screen(Routes.Artist) {
        fun formatRoute(artistId: Long) = Routes.Artist.replace("{artistId}", artistId.toString())
    }


    object Library : Screen(Routes.Library)

    object Playlists : Screen(Routes.Playlists)

    object Search : Screen(Routes.Search)

    object Options : Screen(Routes.Options)


    companion object {
        val NavBar = listOf(Library, Playlists, Search, Options)
    }
}
