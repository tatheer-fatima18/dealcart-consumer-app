package com.localpk.app.features.rewards.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.webview.WebViewScreen
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.BlackText
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.earn_coins
import dealcart.composeapp.generated.resources.game_title_trivia
import dealcart.composeapp.generated.resources.grocery_run
import dealcart.composeapp.generated.resources.ic_trivia_rewards
import dealcart.composeapp.generated.resources.olpy_chase
import dealcart.composeapp.generated.resources.play_games
import org.jetbrains.compose.resources.stringResource

@Composable
fun GamesSection(isLoggedIn: Boolean, userName: String, userToken: String) {
    val navigator = LocalNavigator.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.play_games),
            modifier = Modifier.padding(start = 31.dp),
            style = TextStyle(
                fontSize = 14.sp,
                color = BlackText,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
            )
        )
        Text(
            text = stringResource(Res.string.earn_coins),
            modifier = Modifier.padding(start = 31.dp),
            style = TextStyle(
                color = BlackText,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp
            )
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(22.dp),
        ) {

            if (RemoteConfigs.isGroceryRunGameEnabled()) {
                Column(modifier = Modifier) {
                    GamesCard(
                        backgroundImageResource = Res.drawable.grocery_run,
                        onClick = {
                            val webViewArgs = WebViewScreen(
                                url = "${RemoteConfigs.groceryRunGameLink()}?username=${userName}&token=${userToken}",
                            )
                            if (isLoggedIn) {
                                navigator?.push(
                                    webViewArgs
                                )
                            } else {
                                navigator?.push(
                                    PhoneInputScreen(
                                        navSource = ScreenSource.WEB_VIEW.key
                                    )
                                )
                            }
                        },
                        onEventClick = {}
                    )
                    Text(
                        text = stringResource(Res.string.grocery_run),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp),
                        color = BlackText,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (RemoteConfigs.isDCChaseGameEnabled()) {
                Column(modifier = Modifier) {
                    GamesCard(
                        backgroundImageResource = Res.drawable.olpy_chase,
                        onClick = {
                            val webViewArgs = WebViewScreen(
                                url = "https://rumi019.github.io/dc_chase//?username=${userName}&token=${userToken}",
                            )
                            if (isLoggedIn) {
                                navigator?.push(
                                    webViewArgs
                                )
                            } else {
                                navigator?.push(
                                    PhoneInputScreen(
                                        navSource = ScreenSource.WEB_VIEW.key
                                    )
                                )
                            }
                        },
                        onEventClick = {
                            //  eventLogger.clickOlpyChaseGame()
                        }
                    )
                    Text(
                        text = stringResource(Res.string.olpy_chase),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp),
                        color = BlackText,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (RemoteConfigs.isTraviaGameEnabled()) {
                Column(modifier = Modifier) {
                    GamesCard(
                        backgroundImageResource = Res.drawable.ic_trivia_rewards,
                        onClick = {
                            if (isLoggedIn) {
                                navigator?.push(
                                    WebViewScreen(
                                        url = "https://ahaslides.com/DEALCART",
                                    )
                                )
                            } else {
                                navigator?.push(
                                    PhoneInputScreen(
                                        navSource = ScreenSource.WEB_VIEW.key
                                    )
                                )
                            }
                        },
                        onEventClick = {
                            //  eventLogger.clickTriviaGame()
                        }
                    )
                    Text(
                        text = stringResource(Res.string.game_title_trivia),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp),
                        color = BlackText,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }


        }
        HorizontalDivider(
            Modifier
                .padding(bottom = 16.dp)
                .padding(start = 26.dp)
                .padding(end = 16.dp)
                .fillMaxWidth()
                .height(1.dp),
            color = Color.LightGray
        )
    }
}