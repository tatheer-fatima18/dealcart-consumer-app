package com.localpk.app.features.login.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.changes
import dealcart.composeapp.generated.resources.changes_text
import dealcart.composeapp.generated.resources.cookies
import dealcart.composeapp.generated.resources.cookies_data
import dealcart.composeapp.generated.resources.data_collected
import dealcart.composeapp.generated.resources.data_text
import dealcart.composeapp.generated.resources.intro
import dealcart.composeapp.generated.resources.intro_text
import dealcart.composeapp.generated.resources.overview
import dealcart.composeapp.generated.resources.overview_A
import dealcart.composeapp.generated.resources.overview_A_text
import dealcart.composeapp.generated.resources.overview_B
import dealcart.composeapp.generated.resources.overview_B_text
import dealcart.composeapp.generated.resources.personal_info
import dealcart.composeapp.generated.resources.retention
import dealcart.composeapp.generated.resources.retention_text
import dealcart.composeapp.generated.resources.rights
import dealcart.composeapp.generated.resources.rights_text
import dealcart.composeapp.generated.resources.terms
import dealcart.composeapp.generated.resources.usage_personal
import dealcart.composeapp.generated.resources.usage_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun TermsContent() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TermsSection(
                title = stringResource(Res.string.terms),
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
            title = stringResource(Res.string.intro),
            content = stringResource(Res.string.intro_text)
        )

        TermsSection(
            title = stringResource(Res.string.overview),
            content = null
        )

        TermsSubSection(
            subHeading = stringResource(Res.string.overview_A),
            content = stringResource(Res.string.overview_A_text)
        )

        TermsSubSection(
            subHeading = stringResource(Res.string.overview_B),
            content = stringResource(Res.string.overview_B_text)
        )

        TermsSection(
            title = stringResource(Res.string.personal_info),
            content = null
        )

        TermsSubSection(
            subHeading = stringResource(Res.string.data_collected),
            content = stringResource(Res.string.data_text)
        )

        TermsSubSection(
            subHeading = stringResource(Res.string.usage_personal),
            content = stringResource(Res.string.usage_text)
        )

        TermsSection(
            title = stringResource(Res.string.rights),
            content = stringResource(Res.string.rights_text)
        )

        TermsSection(
            title = stringResource(Res.string.retention),
            content = stringResource(Res.string.retention_text)
        )

        TermsSection(
            title = stringResource(Res.string.cookies),
            content = stringResource(Res.string.cookies_data)
        )

        TermsSection(
            title = stringResource(Res.string.changes),
            content = stringResource(Res.string.changes_text)
        )
    }
}

@Composable
fun TermsSection(
    title: String?,
    content: String?,
    titleStyle: TextStyle = TextStyle(
        fontFamily = PoppinsFontFamily(),
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        fontSize = 14.sp
    )
) {
    if (title != null) {
        Text(
            text = title,
            style = titleStyle,
            modifier = Modifier
                .padding(top = 5.dp)
        )
    }

    content?.let {
        Text(
            text = it,
            style = TextStyle(
                fontSize = 10.sp,
                textAlign = TextAlign.Justify,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = Color.Black
            ),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun TermsSubSection(
    subHeading: String,
    content: String
) {
    Text(
        text = subHeading,
        style = TextStyle(
            fontSize = 10.sp,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        modifier = Modifier.padding(top = 5.dp)
    )

    Text(
        text = content,
        style = TextStyle(
            fontSize = 12.sp,
            textAlign = TextAlign.Justify,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        modifier = Modifier.padding(top = 2.dp)
    )
}