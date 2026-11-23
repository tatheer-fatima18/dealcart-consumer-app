package com.localpk.app.features.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.localpk.app.common_components.AnimatedLottie
import com.localpk.app.features.profile.data.IconSource
import com.localpk.app.features.profile.data.PersonalSectionItem
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.DCLightGrey
import com.localpk.app.theming.DCPrimaryLight
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_arrow_forward_24
import dealcart.composeapp.generated.resources.ic_notification
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import org.jetbrains.compose.resources.painterResource

@Composable
fun SectionGrid(
    items: List<PersonalSectionItem>,
    onItemClick: (PersonalSectionItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(
                border = BorderStroke(1.5.dp, DCLightGrey),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (items.size / 5 + items.size % 5) * 80.dp),
            userScrollEnabled = false
        ) {
            itemsIndexed(items) { _, it ->
                PersonalSectionItem(item = it, onClick = { onItemClick(it) })
            }
        }
    }

}

@Composable
fun PersonalSectionItem(item: PersonalSectionItem, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = 14.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .background(
                    color = DCPrimaryLight,
                    shape = RoundedCornerShape(10.dp)
                ),
        ) {
            // Inner Box to hold icon + padding
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                when (val icon = item.iconSrc) {
                    is IconSource.Drawable -> {
                        Image(
                            painter = painterResource(icon.res),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    is IconSource.Animation -> {
                        AnimatedLottie(
                            modifier = Modifier
                                .width(35.dp)
                                .height(30.dp)
                                .clickable { onClick() },
                            lottieSpec = LottieCompositionSpec.JsonString(icon.json.decodeToString())
                        )
                    }
                }
            }

            if (item.notificationIcon) {
                Image(
                    painter = painterResource(Res.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .width(12.dp)
                        .height(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = item.label,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                color = ColorPrimaryText,
                fontSize = 13.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
            )
        )

        Icon(
            painter = painterResource(Res.drawable.ic_arrow_forward_24),
            contentDescription = null,
            tint = ColorPrimaryText,
            modifier = Modifier
                .width(16.dp)
                .height(16.dp)
        )
    }
}