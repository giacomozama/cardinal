package me.zama.cardinal.utils

import java.time.Duration

fun hmsFromMillis(duration: Long): String {
    val time = Duration.ofMillis(duration)
    return buildString {
        val hours = time.toHours()
        if (hours != 0L) {
            append(hours.toString())
            append(':')
            append(time.toMinutesPart().toString().padStart(2, '0'))
        } else {
            append(time.toMinutesPart())
        }
        append(':')
        append(time.toSecondsPart().toString().padStart(2, '0'))
    }
}