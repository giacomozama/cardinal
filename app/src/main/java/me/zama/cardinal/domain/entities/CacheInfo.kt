package me.zama.cardinal.domain.entities

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.reflect.KClass

@Immutable
@Entity
data class CacheInfo(
    @PrimaryKey
    @ColumnInfo(name = "info_key")
    val key: String,
    val rawValue: String
) {


    companion object {

        private val converters = mapOf<KClass<*>, (String) -> Any>(
            String::class to { it },
            Int::class to { it.toInt() },
            Long::class to { it.toLong() }
        )

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> convert(type: KClass<T>, rawValue: String) = converters[type]?.invoke(rawValue) as? T
            ?: throw UnsupportedOperationException("Can't perform conversion")
    }

    object Keys {
        const val MediaStoreVersion = "media_store_version"
        const val MediaStoreGenerationPrefix = "media_store_generation_"
        const val MediaStoreLastModified = "media_store_last_modified"
    }
}