package me.zama.cardinal.ui.screens.play

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.zama.cardinal.domain.entities.CoverArtImage
import me.zama.cardinal.utils.hmsFromMillis

@Composable
fun PlayScreen(
    viewModel: PlayViewModel
) {
    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()
    val coverArtSize = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.roundToPx()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CoverArtImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            coverArt = currentSong.album.coverArt,
            size = coverArtSize,
            contentDescription = "Cover art for ${currentSong.album.title}"
        )
        PlayControls(
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PlayControls(
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

    }
}

val PlaySeekBarHeight = 8.dp

@Composable
fun PlaySeekBar(
    trackColor: Color,
    elapsed: Long,
    duration: Long
) {
    val elapsedHMS = hmsFromMillis(elapsed)
    val durationHMS = remember(duration) { hmsFromMillis(duration) }
    val fraction = elapsed / duration.toFloat()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(PlaySeekBarHeight)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .weight(fraction)
                .background(trackColor)
        )
        Spacer(modifier = Modifier.weight(1f - fraction))
    }
}