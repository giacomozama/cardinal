package me.zama.cardinal.ui.navigation

import androidx.compose.animation.core.tween

const val DefaultNavTransitionDuration = 300

fun <T> defaultNavAnimationSpec() = tween<T>(durationMillis = DefaultNavTransitionDuration)