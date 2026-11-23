package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.StaticMap
import com.localpk.app.features.restaurant.data.AdditionalDetails
import com.localpk.app.features.restaurant.data.Image
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.capitalizeFirst
import com.localpk.app.utils.convertTo12HourFormat
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.clock_time
import dealcart.composeapp.generated.resources.closed
import dealcart.composeapp.generated.resources.delivery_fee
import dealcart.composeapp.generated.resources.hours
import dealcart.composeapp.generated.resources.map_pin_line
import dealcart.composeapp.generated.resources.minimum_order
import dealcart.composeapp.generated.resources.restaurant_information
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantInfoBottomSheet(
    onClose: () -> Unit,
    additionalDetails: AdditionalDetails,
    minimumOrderValue: Int,
    deliveryFee: Long,
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val dayOfWeek = today.dayOfWeek.name.lowercase()

    ModalBottomSheet(
        onDismissRequest = { onClose() },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        dragHandle = null,
        containerColor = Color.Transparent,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        onClose()
                    },
                    modifier = Modifier
                        .background(Color(0xFF3A3A56), shape = CircleShape)
                        .width(48.dp)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .background(
                        Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.restaurant_information),
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = TextSubdued
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (additionalDetails.timings.isNullOrEmpty().not()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(Res.drawable.clock_time),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(Res.string.hours),
                            style = TextStyle(
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = TextSubdued
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                additionalDetails.timings?.forEach { time ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = time.day.default().capitalizeFirst(),
                            modifier = Modifier.width(80.dp),
                            style = TextStyle(
                                fontFamily = PoppinsFontFamily(),
                                fontSize = 12.sp,
                                color = if (time.day == dayOfWeek) TextSubdued else TextSubduedHalf,
                                fontWeight = if (time.day == dayOfWeek) FontWeight.SemiBold else FontWeight.Normal
                            ),
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = if (time.isOpen.default()) "${convertTo12HourFormat(time.openingTime.default())} - ${
                                convertTo12HourFormat(
                                    time.closingTime.default()
                                )
                            }" else stringResource(Res.string.closed),
                            style = TextStyle(
                                fontFamily = PoppinsFontFamily(),
                                fontSize = 12.sp,
                                color = if (time.day == dayOfWeek) TextSubdued else TextSubduedHalf,
                                fontWeight = if (time.day == dayOfWeek) FontWeight.SemiBold else FontWeight.Normal
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Image(
                        painter = painterResource(Res.drawable.map_pin_line),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = additionalDetails.address?.completeAddress().default(),
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = TextSubdued
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                val initialLat = additionalDetails.address?.latitude.getValueOrDefault()
                val initialLng = additionalDetails.address?.longitude.getValueOrDefault()

                if (initialLat != 0.0 && initialLng != 0.0) {
                    val initialLocation = Geolocation(initialLat, initialLng)

                    Spacer(modifier = Modifier.height(8.dp))

                    StaticMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(shape = RoundedCornerShape(12.dp)),
                        location = initialLocation
                    )
                }

                if (deliveryFee.isNotZero()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        stringResource(Res.string.delivery_fee),
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = TextSubdued
                        ),
                    )
                    Text(
                        "Rs $deliveryFee",
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = TextSubdued
                        ),
                    )
                }

                if (minimumOrderValue.isNotZero()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        stringResource(Res.string.minimum_order),
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = TextSubdued
                        ),
                    )
                    Text(
                        "Rs $minimumOrderValue",
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = TextSubdued
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}