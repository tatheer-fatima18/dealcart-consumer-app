package com.localpk.app.features.share_and_earn.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.share_and_earn.data.ShareIcon
import com.localpk.app.theming.DealcartGreen
import com.localpk.app.theming.LightGray
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.PurpleNew
import com.localpk.app.utils.copyTextToClipboardAndShowToast
import com.localpk.app.utils.copyToClipboard
import com.localpk.app.common_components.CustomSnackbar
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.copy
import dealcart.composeapp.generated.resources.ic_attachment
import dealcart.composeapp.generated.resources.invite_friend
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShareAndEarnInvitation(shareIcons: List<ShareIcon>, link: String, showSnackbar: () -> Unit = {}) {
    val isLoading = link.isEmpty()

    
    Box {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val stroke = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), 0f)
            )
            Text(
                text = stringResource(Res.string.invite_friend),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = PurpleNew
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                shareIcons.forEach {
                    IconButton(onClick = { it.onClick() }) {
                        Image(
                            modifier = Modifier.size(37.dp),
                            painter = painterResource(it.icon),
                            contentDescription = null
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFF17DC95),
                            style = stroke,
                            cornerRadius = CornerRadius(37.88443.dp.toPx())
                        )
                    }
                    .clip(RoundedCornerShape(37.88443.dp))
                    .background(
                        color = Color(0x0D01BE7A),
                        shape = RoundedCornerShape(size = 37.88443.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier,
                        painter = painterResource(Res.drawable.ic_attachment),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        modifier = Modifier.weight(1.0f),
                        text = link,
                        style = TextStyle(
                            fontSize = 9.02.sp,
                            lineHeight = 9.02.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = DealcartGreen
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
//                        if (isLoading) {
//                            LoaderScreen()
//                        }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        modifier = Modifier
                            .background(
                                if (link.isEmpty()) LightGray else DealcartGreen,
                                RoundedCornerShape(21.64824.dp)
                            )
                            .clickable {
                                if (link.isNotEmpty()) {
                                    copyToClipboard(link)
                                    showSnackbar()
                                }
                            }
                            .padding(12.dp, 2.dp),
                        text = stringResource(Res.string.copy),
                        style = TextStyle(
                            fontSize = 9.02.sp,
                            lineHeight = 9.02.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )
                }
            }
        }
        
        // Custom Snackbar for "Copied" message

    }
}