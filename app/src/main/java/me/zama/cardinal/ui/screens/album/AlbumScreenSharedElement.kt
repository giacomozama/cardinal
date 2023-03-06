package me.zama.cardinal.ui.screens.album

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.lerp
import me.zama.cardinal.domain.entities.CoverArtImage
import me.zama.cardinal.ui.navigation.defaultNavAnimationSpec

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun AlbumScreenSharedElement(
    transition: Transition<EnterExitState>,
    mode: AlbumScreenMode,
    headerPaddingPx: Int,
    headerHeightPxState: State<Int>,
    fraction: Float,
    finalCoverSize: Int
) {
    val density = LocalDensity.current

    val coverOffset by transition.animateIntOffset(
        label = "albumScreenCoverOffset",
        transitionSpec = { defaultNavAnimationSpec() }
    ) { state ->
        when (state) {
            EnterExitState.PreEnter, EnterExitState.PostExit -> mode.origin.offset
            EnterExitState.Visible -> IntOffset(x = headerPaddingPx, y = headerPaddingPx)
        }
    }

    val coverSize by transition.animateInt(
        label = "albumScreenCoverSize",
        transitionSpec = { defaultNavAnimationSpec() }
    ) { state ->
        when (state) {
            EnterExitState.PreEnter, EnterExitState.PostExit -> mode.origin.size
            EnterExitState.Visible -> headerHeightPxState.value - headerPaddingPx * 2
        }
    }

    val coverTopCornerRadius by transition.animateDp(
        label = "albumScreenTopCornerRadius",
        transitionSpec = { defaultNavAnimationSpec() }
    ) { state ->
        when (state) {
            EnterExitState.PreEnter, EnterExitState.PostExit -> mode.origin.topCornerRadius
            EnterExitState.Visible -> lerp(
                start = AlbumScreenCoverMinCornerRadius,
                stop = AlbumScreenCoverMaxCornerRadius,
                fraction = fraction
            )
        }
    }

    val coverBottomCornerRadius by transition.animateDp(
        label = "albumScreenBottomCornerRadius",
        transitionSpec = { defaultNavAnimationSpec() }
    ) { state ->
        when (state) {
            EnterExitState.PreEnter, EnterExitState.PostExit -> mode.origin.bottomCornerRadius
            EnterExitState.Visible -> lerp(
                start = AlbumScreenCoverMinCornerRadius,
                stop = AlbumScreenCoverMaxCornerRadius,
                fraction = fraction
            )
        }
    }

    val coverShape = RoundedCornerShape(
        topStart = coverTopCornerRadius,
        topEnd = coverTopCornerRadius,
        bottomStart = coverBottomCornerRadius,
        bottomEnd = coverBottomCornerRadius
    )

    Box(
        modifier = Modifier
            .offset { coverOffset }
            .size(with(density) { coverSize.toDp() })
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = coverShape
        ) {
            CoverArtImage(
                modifier = Modifier.fillMaxSize(),
                coverArt = mode.origin.coverArt,
                size = finalCoverSize,
                thumbnailSize = mode.origin.size,
                contentDescription = null
            )
        }
    }
}
