package com.localpk.app.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.PoppinsFontFamily

@Composable
fun MatchInfoHeader(mainSponsor: PoweredByDetailUiData?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = RemoteConfigs.getTournamentName(),
            color = Color.White,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = "x",
            color = Color.White,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        if (mainSponsor != null) {
            if (mainSponsor.image?.medium.isNullOrEmpty().not()) {
                Image(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    painter = rememberAsyncImagePainter(mainSponsor.image?.medium),
                    contentDescription = "Sponsor Logo"
                )
            }

            Text(
                text = mainSponsor.name,
                color = Color.White,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}