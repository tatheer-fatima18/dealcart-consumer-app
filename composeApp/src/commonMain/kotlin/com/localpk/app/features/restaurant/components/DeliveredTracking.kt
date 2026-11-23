package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.theming.BarlowCondensedFontFamily
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.call_restaurant
import dealcart.composeapp.generated.resources.delivered_desc
import dealcart.composeapp.generated.resources.fallback_logo
import dealcart.composeapp.generated.resources.is_order_in_progress
import dealcart.composeapp.generated.resources.need_help
import dealcart.composeapp.generated.resources.order_delivered
import dealcart.composeapp.generated.resources.order_details
import dealcart.composeapp.generated.resources.out_for_Delivery
import dealcart.composeapp.generated.resources.out_for_Delivery_desc
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeliveredStatus(
    startLatLng: Geolocation,
    endLatLng: Geolocation,
    outForDelStatus: Boolean,
    deliveredStatus: Boolean,
    onCall: () -> Unit,
    onHelp: () -> Unit,
    onBack: () -> Unit,
    onClickOrders: () -> Unit,
    path: List<Geolocation>?,
    deliveryTime: String,
    logo: String,
    isCheckOut: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(
                RoundedCornerShape(
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp,
                    topStart = 8.dp,
                    topEnd = 8.dp
                )
            )
    ) {
//        Map(
//            startLatLng = startLatLng,
//            endLatLng = endLatLng,
//            path = path,
//            logo = logo
//        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.9f),
                            Color.White
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (deliveredStatus)
                    loadImagePainterWithValidation(
                        imageUrl = logo,
                        fallbackRes = Res.drawable.fallback_logo,
                        contentDescription = null,
                        modifier = Modifier
                            .size(84.dp)
                            .clip(shape = CircleShape)
                    )
                else
                    Image(
                        modifier = Modifier
                            .size(84.dp),
                        painter = painterResource(Res.drawable.is_order_in_progress),
                        contentDescription = null,
                    )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(if (deliveredStatus) Res.string.order_delivered else Res.string.out_for_Delivery),
                    style = TextStyle(
                        color = ButtonSurfaceBrand,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 18.sp
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(if (deliveredStatus) Res.string.delivered_desc else Res.string.out_for_Delivery_desc),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 30.dp),
                    style = TextStyle(
                        color = TextSubdued,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 14.sp
                    ),
                )

                if (outForDelStatus) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = deliveryTime,
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = BarlowCondensedFontFamily(),
                            fontWeight = FontWeight.Bold,
                            color = ButtonSurfaceBrand,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(Res.string.need_help),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = TextSubdued,
                    ),
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .background(
                            color = ButtonSurfaceBrand,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onCall() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.call_restaurant).uppercase(),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .background(
                            color = SurfaceBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onClickOrders() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.order_details).uppercase(),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = TextSubdued
                        )
                    )
                }
            }
        }
        Top(onBack, onHelp, isCheckOut)
    }
}