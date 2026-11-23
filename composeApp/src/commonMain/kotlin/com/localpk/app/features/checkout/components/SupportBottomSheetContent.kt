package com.localpk.app.features.checkout.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.login.components.TermsSection
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.last_part
import dealcart.composeapp.generated.resources.our_mission
import dealcart.composeapp.generated.resources.our_mission_A
import dealcart.composeapp.generated.resources.our_mission_A_text
import dealcart.composeapp.generated.resources.our_mission_B_text
import dealcart.composeapp.generated.resources.our_mission_C
import dealcart.composeapp.generated.resources.our_mission_C_text
import dealcart.composeapp.generated.resources.our_mission_D
import dealcart.composeapp.generated.resources.our_mission_D_text
import dealcart.composeapp.generated.resources.our_mission_text
import dealcart.composeapp.generated.resources.what_we_are_doing
import dealcart.composeapp.generated.resources.why_we_need_your_help
import dealcart.composeapp.generated.resources.why_we_need_your_help_text
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.text.ClickableText

@Composable
fun SupportBottomSheetContent() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TermsSection(
                title = stringResource(Res.string.our_mission),
                content = null,
                titleStyle = TextStyle(
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            )
        }

        TermsSection(
            title = null,
            content = stringResource(Res.string.our_mission_text)
        )

        TermsSection(
            title = stringResource(Res.string.what_we_are_doing),
            content = stringResource(Res.string.our_mission_A_text),
            titleStyle = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        )

        TermsSection(
            title = stringResource(Res.string.why_we_need_your_help),
            content = stringResource(Res.string.why_we_need_your_help_text),
            titleStyle = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        )

        TermsSection(
            title = stringResource(Res.string.last_part),
            content = null,
            titleStyle = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        )

        TermsSection(
            title = stringResource(Res.string.our_mission_A),
            content = stringResource(Res.string.our_mission_B_text),
            titleStyle = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        )

        TermsSection(
            title = stringResource(Res.string.our_mission_C),
            content = stringResource(Res.string.our_mission_C_text),
            titleStyle = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        )

        TermsSection(
            title = stringResource(Res.string.our_mission_D),
            content = stringResource(Res.string.our_mission_D_text),
            titleStyle = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        )

        SaylaniLinkText()
    }
}

@Composable
fun SaylaniLinkText() {
    val uriHandler = LocalUriHandler.current

    val annotatedText = buildAnnotatedString {
        append("Learn more about Saylani’s work here: ")

        // Add URL
        val url = "https://saylaniwelfare.com/"
        pushStringAnnotation(tag = "URL", annotation = url)
        withStyle(
            style = SpanStyle(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textDecoration = TextDecoration.Underline
            ),
        ) {
            append(url)
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        },
        style = TextStyle(
            fontSize = 12.sp,
            textAlign = TextAlign.Justify,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    )
}
