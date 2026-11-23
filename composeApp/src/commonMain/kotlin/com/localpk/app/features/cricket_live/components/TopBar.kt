package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_back_arrow
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopBarCricket(
    mainSponsor: PoweredByDetailUiData?,
    onBackClick: () -> Unit
) {
    Surface(
        color = ColorTermCondition,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Image(
                    painter = painterResource(Res.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = RemoteConfigs.getTournamentName(),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFontFamily()
                )
                Text(
                    text = "x",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily(),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (mainSponsor != null) {
                    if (mainSponsor.image?.medium.isNullOrEmpty().not()) {
                        Image(
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp),
                            painter = rememberAsyncImagePainter(mainSponsor.image?.medium),
                            contentDescription = "Sponsor Logo"
                        )
                        // Sponsor image would go here - using placeholder for now
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = mainSponsor.name.default(),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PoppinsFontFamily()
                    )
                }
            }
        }
    }
}
