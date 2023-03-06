package me.zama.cardinal.ui.glide

import android.content.Context
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.signature.ObjectKey
import me.zama.cardinal.domain.entities.CoverArt
import java.io.InputStream

@GlideModule
class CardinalGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(CoverArt::class.java, InputStream::class.java, CoverArtModelLoader.Factory())
    }
}

class CoverArtModelLoader : ModelLoader<CoverArt, InputStream> {

    private class CoverArtDataFetcher(private val model: CoverArt) : DataFetcher<InputStream> {

        private lateinit var inputStream: InputStream

        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
            inputStream = model.uri!!.toUri().toFile().inputStream()
        }

        override fun cleanup() {
            inputStream.close()
        }

        override fun cancel() {
        }

        override fun getDataClass() = InputStream::class.java

        override fun getDataSource() = DataSource.LOCAL
    }

    override fun buildLoadData(
        model: CoverArt,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream> {
        val key = ObjectKey(model.uri!! + "_${width}_${height}")
        return ModelLoader.LoadData(key, CoverArtDataFetcher(model))
    }

    override fun handles(model: CoverArt) = model.uri != null

    class Factory : ModelLoaderFactory<CoverArt, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<CoverArt, InputStream> {
            return CoverArtModelLoader()
        }

        override fun teardown() {
        }
    }
}