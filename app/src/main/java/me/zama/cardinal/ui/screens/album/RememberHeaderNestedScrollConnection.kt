package me.zama.cardinal.ui.screens.album

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity

@Composable
fun rememberHeaderNestedScrollConnection(
    expandedHeaderHeightPx: Int,
    heightState: MutableState<Int>
): NestedScrollConnection {
    val density = LocalDensity.current
    return remember {
        val collpasedHeightPx = with(density) { HeaderMinHeight.roundToPx() }

        heightState.value = expandedHeaderHeightPx

        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                when {
                    available.y < -10 -> heightState.value = collpasedHeightPx
                    available.y > 10 -> heightState.value = expandedHeaderHeightPx
                }
//                heightState.value = (heightState.value + available.y.toInt())
//                    .coerceIn(collpasedHeightPx, expandedHeaderHeightPx)
                return super.onPreScroll(available, source)
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                return super.onPreFling(available)
            }
        }
    }
}