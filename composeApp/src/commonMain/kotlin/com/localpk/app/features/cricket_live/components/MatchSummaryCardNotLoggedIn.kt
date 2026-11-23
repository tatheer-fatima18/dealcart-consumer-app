package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.localpk.app.common_components.loadComposableAsyncImage
import com.localpk.app.features.cricket_live.data.CricketSwimlaneUiData
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.Horizontal_Separator
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.Team2ScoreCard
import com.localpk.app.theming.VersesScoreBg
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.live
import dealcart.composeapp.generated.resources.match
import dealcart.composeapp.generated.resources.pakistan_logo
import dealcart.composeapp.generated.resources.powered_by
import dealcart.composeapp.generated.resources.scores_aur_doosri_maloomat_jaan_ne_kay_liye_login_karein
import dealcart.composeapp.generated.resources.vs
import org.jetbrains.compose.resources.stringResource

@Composable
fun MatchSummaryCardNotLoggedIn(
    matchData: CricketSwimlaneUiData,
    onLoginClick: () -> Unit,
    scorecardSponsor: PoweredByDetailUiData?,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            // Similar structure to logged in version but simpler
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.match),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily()
                )
                if (matchData.isLive) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFDA1414),
                                shape = RoundedCornerShape(9.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.live).uppercase(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (scorecardSponsor != null) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(Res.string.powered_by),
                            fontSize = 10.sp,
                            color = Color.Black,
                            fontFamily = PoppinsFontFamily()
                        )

                        Image(
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp),
                            painter = rememberAsyncImagePainter(scorecardSponsor.image?.medium),
                            contentDescription = "Sponsor Logo"
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    loadComposableAsyncImage(
                        imageUrl = matchData.flagTeamA,
                        fallbackRes = Res.drawable.pakistan_logo,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = matchData.teamA,
                        fontSize = 14.sp,
                        color = Team2ScoreCard,
                        maxLines = 2,
                        fontFamily = PoppinsFontFamily()
                    )
                }

                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.vs),
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 10.sp,
                        color = ColorTermCondition,
                        modifier = Modifier
                            .background(
                                color = VersesScoreBg,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 6.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1f)
                ) {
                    loadComposableAsyncImage(
                        imageUrl = matchData.flagTeamB,
                        fallbackRes = Res.drawable.pakistan_logo,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = matchData.teamB,
                        fontSize = 14.sp,
                        color = Team2ScoreCard,
                        maxLines = 2,
                        fontFamily = PoppinsFontFamily()
                    )

                }
            }

            if (matchData.isLive) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Horizontal_Separator)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.scores_aur_doosri_maloomat_jaan_ne_kay_liye_login_karein),
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = ColorTermCondition
                ),
                border = BorderStroke(1.dp, ColorTermCondition)
            ) {
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White
                )
            }
        }
    }
}


