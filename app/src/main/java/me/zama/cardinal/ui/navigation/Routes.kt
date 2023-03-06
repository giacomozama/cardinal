package me.zama.cardinal.ui.navigation

object Routes {

    const val InnerHost = "innerHost"
    const val Library = "library"
    const val Playlists = "playlists"
    const val Search = "search"
    const val Options = "options"
    const val Play = "play"
    const val Album = "album/{albumId}"
    const val Artist = "artist/{artistId}"

    fun withArguments(route: String, vararg arguments: Pair<String, Any?>): String {
        val argsMap = arguments.toMap()
        @Suppress("RegExpRedundantEscape") // the escape is not actually redundant
        return route.replace("""\{([a-zA-Z\d]+)\}""".toRegex()) {
            argsMap[it.groupValues[1]].toString()
        }
    }
}