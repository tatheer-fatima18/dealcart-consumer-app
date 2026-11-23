package com.localpk.app.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BlackSecondary
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.ButtonSurfaceSubdued
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundGhosted
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.dccoins
import dealcart.composeapp.generated.resources.use_dc_coins
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DCCoinsView(
    amountInCoins: String,
    amountInRupees: String,
    isDCCoinEnabled: Boolean,
    isDiscountSwitchChecked: Boolean,
    dcCoinErrorMessage: String,
    dcCoinWarningTrigger: Boolean,
    onDCCoinsSwitchClicked: (checked: Boolean) -> Unit,
    useDCCoinsAmountLimit: Int,
    cartTotal: Double,
    hasRestrictedPromotionalDiscountDC: Boolean,
    hasRestrictedPromotionalDiscountMultiCity: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .border(
                width = if (dcCoinWarningTrigger) 1.dp else 0.dp,
                color = if (dcCoinWarningTrigger) IconDanger else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(all = 8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.use_dc_coins),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextSubdued,
                )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.dccoins),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        append(
                            amountInCoins
                        )
                        append(
                            " = "
                        )
                        append(
                            amountInRupees
                        )
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = BlackSecondary
                    )
                )
            }
            Box {
                DCCoinsSwitch(
                    isDCCoinEnabled = isDCCoinEnabled,
                    checked = isDiscountSwitchChecked,
                    onCheckedChange = { onDCCoinsSwitchClicked(it) },
                    trackWidth = 48.dp,  // Custom width of the track
                    trackHeight = 24.dp, // Custom height of the track
                    thumbSize = 18.dp,   // Custom size of the thumb
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = ButtonSurfaceBrand,
                        uncheckedThumbColor = ButtonSurfaceSubdued,
                        uncheckedTrackColor = SurfaceBackgroundGhosted
                    )
                )
                if (cartTotal <= useDCCoinsAmountLimit || hasRestrictedPromotionalDiscountDC || hasRestrictedPromotionalDiscountMultiCity)
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { onDCCoinsSwitchClicked(false) }
                    )
            }


        }
        if (dcCoinWarningTrigger) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = dcCoinErrorMessage,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = IconDanger,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}