package com.localpk.app.features.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.profile.data.PersonalSectionItem
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.meri_profile
import dealcart.composeapp.generated.resources.support
import org.jetbrains.compose.resources.stringResource

@Composable
fun PersonalSupportSections(
    onPersonalItemClick: (PersonalSectionItem) -> Unit,
    onSupportItemClick: (PersonalSectionItem) -> Unit,
    personalSectionItemList: List<PersonalSectionItem>,
    supportSectionItemList: List<PersonalSectionItem>
) {
    Spacer(modifier = Modifier.height(8.dp))
    Column(modifier = Modifier.padding(16.dp)) {
        if (personalSectionItemList.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.meri_profile),
                style = TextStyle(
                    letterSpacing = 0.5.sp,
                    fontSize = 17.sp,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            SectionGrid(
                items = personalSectionItemList,
                onItemClick = onPersonalItemClick
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
        Text(
            text = stringResource(Res.string.support),
            style = TextStyle(
                letterSpacing = 0.5.sp,
                fontSize = 17.sp,
                color = Color.Black,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        SectionGrid(
            items = supportSectionItemList,
            onItemClick = onSupportItemClick
        )
    }
}