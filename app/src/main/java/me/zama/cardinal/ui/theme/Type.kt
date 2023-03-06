package me.zama.cardinal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import me.zama.cardinal.R

val Montserrat = FontFamily(
    Font(
        resId = R.font.montserrat_thin,
        weight = FontWeight.W100
    ),
    Font(
        resId = R.font.montserrat_extralight,
        weight = FontWeight.W200
    ),
    Font(
        resId = R.font.montserrat_light,
        weight = FontWeight.W300
    ),
    Font(
        resId = R.font.montserrat_regular,
        weight = FontWeight.W400
    ),
    Font(
        resId = R.font.montserrat_medium,
        weight = FontWeight.W500
    ),
    Font(
        resId = R.font.montserrat_semibold,
        weight = FontWeight.W600
    ),
    Font(
        resId = R.font.montserrat_bold,
        weight = FontWeight.W700
    ),
    Font(
        resId = R.font.montserrat_extrabold,
        weight = FontWeight.W800
    ),
    Font(
        resId = R.font.montserrat_black,
        weight = FontWeight.W900
    )
)

val Typography = Typography()

/*
val Typography = with(Typography()) {
    Typography(
        displayLarge = displayLarge.copy(fontFamily = Montserrat),
        displayMedium = displayMedium.copy(fontFamily = Montserrat),
        displaySmall = displaySmall.copy(fontFamily = Montserrat),
        headlineLarge = headlineLarge.copy(fontFamily = Montserrat),
        headlineMedium = headlineMedium.copy(fontFamily = Montserrat),
        headlineSmall = headlineSmall.copy(fontFamily = Montserrat),
        titleLarge = titleLarge.copy(fontFamily = Montserrat),
        titleMedium = titleMedium.copy(fontFamily = Montserrat),
        titleSmall = titleSmall.copy(fontFamily = Montserrat),
        bodyLarge = bodyLarge.copy(fontFamily = Montserrat),
        bodyMedium = bodyMedium.copy(fontFamily = Montserrat),
        bodySmall = bodySmall.copy(fontFamily = Montserrat),
        labelLarge = labelLarge.copy(fontFamily = Montserrat),
        labelMedium = labelMedium.copy(fontFamily = Montserrat),
        labelSmall = labelSmall.copy(fontFamily = Montserrat)
    )
}
*/
