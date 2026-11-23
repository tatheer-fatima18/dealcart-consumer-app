package com.localpk.app.features.cart.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BundleBg
import com.localpk.app.theming.BundleBorder
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SwitchGrey
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.apnay_cart
import dealcart.composeapp.generated.resources.cart_ko
import dealcart.composeapp.generated.resources.create_my_ramadan_bundle
import dealcart.composeapp.generated.resources.switch_to_normal_order
import org.jetbrains.compose.resources.stringResource

@Composable
fun BundleToggleSwitch(
    isFlashDealList: Boolean,
    isBundle: Boolean
) {
    val checked = isBundle.default()
    val isToggle by remember { mutableStateOf(checked) }
    var select by remember { mutableStateOf(isToggle) }
    val text =
        stringResource(if (checked) Res.string.switch_to_normal_order else Res.string.create_my_ramadan_bundle)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BundleBg,
                shape = RoundedCornerShape(16.dp),
            )
            .border(
                1.dp, BundleBorder,
                RoundedCornerShape(16.dp)
            )
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = TextStyle(
                    color = ColorPrimary,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(top = 5.dp, start = 3.dp)
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = stringResource(if (checked) Res.string.cart_ko else Res.string.apnay_cart),
                style = TextStyle(
                    color = TextSubdued,
                    fontSize = 10.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(bottom = 3.dp, start = 3.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Box(
            modifier = Modifier
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .width(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        select = isToggle
//                        if (isFlashDealList && checked.not()) {
//                            showFavouritesSuccess(
//                                R.drawable.ic_dc_sad_orange,
//                                stringsResourceManager.getString(R.string.remove_flash_deals_from_the_cart)
//                            )
//                        } else {
//                            cartViewModel.bundleSwitch()
//                        }
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (checked) ColorPrimary else SwitchGrey),
                    contentAlignment = if (checked) Alignment.TopEnd else Alignment.TopStart
                ) {
                    CheckCircle(modifier = Modifier.padding(5.dp))
                }
            }
        }
    }
}