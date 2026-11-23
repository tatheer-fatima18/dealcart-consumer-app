package com.localpk.app.features.home.components.swimlanes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.swimlane_engine.data.PromotionalCategory
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued

import org.jetbrains.compose.resources.painterResource
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.promotional_icon

@Composable
fun PromotionalCategoriesSwimlaneComponent(
    promotionalCategories: List<PromotionalCategory>,
    title: String,
    subTitle: String,
    onCategoryClick: (PromotionalCategory) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .padding(start = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.width(250.dp),
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued, // Use theme color

                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                if (subTitle.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .width(280.dp)
                            .padding(bottom = 8.dp),
                        text = subTitle,
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = TextPrimary, // Use theme color

                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.weight(.1f))

           
            Image(
                painter = painterResource(Res.drawable.promotional_icon),
                contentDescription = null,
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                contentScale = ContentScale.Crop
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(promotionalCategories) { _, it ->
                PromotionalCategoryCard(it) {
                     onCategoryClick(it)
                 }
            }
        }
    }
} 