package me.zama.cardinal.domain.entities

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color.argb
import android.media.MediaMetadataRetriever
import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils.blendARGB
import androidx.core.graphics.ColorUtils.calculateContrast
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import androidx.room.ColumnInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.zama.cardinal.data.mediastore.MediaStoreQuery
import me.zama.cardinal.ui.DefaultCoverArt
import me.zama.cardinal.ui.screens.library.albums.AlbumsGridSpacing
import java.io.File

@Immutable
data class CoverArt(
    @ColumnInfo(name = "background_color")
    val backgroundColorArgb: Int?,
    val uri: String?
) {

    companion object {

        val Default = CoverArt(null, null)

        private fun getFileName(albumId: Long): String = "album_art_$albumId.jpg"

        private fun getCacheDir(context: Context): File {
            val dir = File(context.filesDir, "cover_art")
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

        suspend fun create(context: Context, songId: Long, albumId: Long): CoverArt? {
            val file = File(getCacheDir(context), getFileName(albumId))

            val palette = withContext(Dispatchers.IO) {
                val bitmap = MediaMetadataRetriever().use { retriever ->
                    val uri = ContentUris.withAppendedId(MediaStoreQuery.CollectionUri, songId)
                    retriever.setDataSource(context, uri)
                    retriever.embeddedPicture?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                } ?: return@withContext null

                file.delete()
                file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 80, it) }

                Palette.Builder(bitmap).generate()
            } ?: return null

            val dominantSwatch = palette.dominantSwatch ?: return CoverArt(
                backgroundColorArgb = null,
                uri = file.path
            )

            return CoverArt(
                backgroundColorArgb = argb(
                    255,
                    dominantSwatch.rgb.red,
                    dominantSwatch.rgb.green,
                    dominantSwatch.rgb.blue
                ),
                uri = file.toUri().toString()
            )
        }
    }
}

val CoverArt?.backgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() {
        val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
        val bg = this?.backgroundColorArgb ?: return surfaceColor
        var blend = blendARGB(bg, surfaceColor.toArgb(), .5f)
        val contrastWithBg = calculateContrast(blend, MaterialTheme.colorScheme.background.toArgb())
        if (contrastWithBg < 1.5) {
            blend = blendARGB(blend, MaterialTheme.colorScheme.onBackground.toArgb(), .1f)
        }
        return Color(blend)
    }

val CoverArt?.titleTextColor
    @Composable
    @ReadOnlyComposable
    get() = this?.backgroundColorArgb?.let {
        Color(blendARGB(it, MaterialTheme.colorScheme.onSurface.toArgb(), .9f))
    } ?: MaterialTheme.colorScheme.onSurface

val CoverArt?.bodyTextColor
    @Composable
    @ReadOnlyComposable
    get() = this?.backgroundColorArgb?.let {
        Color(blendARGB(it, MaterialTheme.colorScheme.onSurface.toArgb(), .5f))
    } ?: MaterialTheme.colorScheme.onSurfaceVariant

enum class CoverArtSize(val suffix: String, private val divideWidthBy: Int) {
    Small("_s", 4),
    Medium("_m", 3),
    Large("_l", 2);

    @Composable
    @Px
    fun toPx(): Int {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val density = LocalDensity.current
        return remember(screenWidth) {
            with(density) {
                ((screenWidth - AlbumsGridSpacing * (divideWidthBy + 1)) / divideWidthBy).roundToPx()
            }
        }
    }
}

//fun CoverArt.coilCacheKey(size: CoverArtSize) = uri!!.toString() + size.suffix

//@Composable
//fun CoverArt?.painter(
//    size: CoverArtSize,
//    modelBuilder: (ImageRequest.Builder.() -> ImageRequest.Builder)? = null
//): Painter {
//    val uri = this?.uri ?: return rememberVectorPainter(image = DefaultCoverArt)
//    val cacheKey = coilCacheKey(size)
//    val sizePx = size.toPx()
//    return rememberAsyncImagePainter(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(uri)
//            .size(sizePx, sizePx)
//            .memoryCacheKey(cacheKey)
//            .diskCacheKey(cacheKey)
//            .let { modelBuilder?.invoke(it) ?: it }
//            .build(),
//        contentScale = ContentScale.Crop,
//        filterQuality = FilterQuality.None
//    )
//}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CoverArtImage(
    modifier: Modifier = Modifier,
    coverArt: CoverArt?,
    @Px size: Int,
    @Px thumbnailSize: Int? = null,
    contentDescription: String?,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (coverArt?.uri == null) {
        Image(
            modifier = modifier,
            painter = rememberVectorPainter(image = DefaultCoverArt),
            contentDescription = contentDescription,
            alignment = alignment,
            contentScale = contentScale
        )
    } else {
        val context = LocalContext.current

        GlideImage(
            model = coverArt.uri,
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            failure = placeholder {
                Spacer(modifier)
            }
        ) { builder ->
            builder
                .run {
                    if (thumbnailSize != null) {
                        val requestManger = Glide.get(context).requestManagerRetriever[context]
                        thumbnail(
                            requestManger.asDrawable()
                                .load(coverArt.uri)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .onlyRetrieveFromCache(true)
                                .signature(ObjectKey(coverArt.toString() + thumbnailSize))
                                .override(thumbnailSize)
                        )
                    } else {
                        this
                    }
                }
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .signature(ObjectKey(coverArt.toString() + size))
                .override(size)
        }
    }
}
