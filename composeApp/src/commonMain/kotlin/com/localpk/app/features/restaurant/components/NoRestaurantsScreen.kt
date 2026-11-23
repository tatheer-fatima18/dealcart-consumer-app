package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.capitalizeFirst
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.change_location
import dealcart.composeapp.generated.resources.no_restaurants
import dealcart.composeapp.generated.resources.no_restaurants_sub_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoRestaurantsScreen(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 10.dp, vertical = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.no_restaurants),
            contentDescription = null,
            modifier = Modifier
                .size(126.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(Res.string.no_restaurants),
            modifier = Modifier.padding(horizontal = 80.dp),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = TextSubdued,
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.no_restaurants_sub_title),
            modifier = Modifier.padding(horizontal = 80.dp),
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = TextSubdued,
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .background(
                    color = ButtonSurfaceBrand,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onClick() }
                .padding(vertical = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.change_location).capitalizeFirst(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            )
        }
    }
}