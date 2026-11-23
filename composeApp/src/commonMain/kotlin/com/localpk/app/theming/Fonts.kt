package com.localpk.app.theming

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.barlow_condensed_bold
import dealcart.composeapp.generated.resources.barlow_condensed_light
import dealcart.composeapp.generated.resources.nunito_bold
import dealcart.composeapp.generated.resources.nunito_medium
import dealcart.composeapp.generated.resources.nunito_regular
import dealcart.composeapp.generated.resources.nunito_semibold
import dealcart.composeapp.generated.resources.poppins_bold
import dealcart.composeapp.generated.resources.poppins_medium
import dealcart.composeapp.generated.resources.poppins_regular
import dealcart.composeapp.generated.resources.poppins_semibold
import dealcart.composeapp.generated.resources.roboto_condensed_medium
import org.jetbrains.compose.resources.Font

@Composable
fun PoppinsFontFamily() = FontFamily(
    Font(Res.font.poppins_regular, FontWeight.Normal),
    Font(Res.font.poppins_medium, FontWeight.Medium),
    Font(Res.font.poppins_semibold, FontWeight.SemiBold),
    Font(Res.font.poppins_bold, FontWeight.Bold),
)

@Composable
fun NunitoFontFamily() = FontFamily(
    Font(Res.font.nunito_bold, FontWeight.Bold),
    Font(Res.font.nunito_regular, FontWeight.Normal),
    Font(Res.font.nunito_medium, FontWeight.Medium),
    Font(Res.font.nunito_semibold, FontWeight.SemiBold),
)

@Composable
fun BarlowCondensedFontFamily() = FontFamily(
    Font(Res.font.barlow_condensed_bold, FontWeight.Bold),
    Font(Res.font.barlow_condensed_light, FontWeight.Normal)
)

@Composable
fun RobotoCondensedFamily() = FontFamily(
    Font(Res.font.roboto_condensed_medium, FontWeight.Medium)
)