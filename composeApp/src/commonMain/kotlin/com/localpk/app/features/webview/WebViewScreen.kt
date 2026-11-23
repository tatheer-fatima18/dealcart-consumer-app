package com.localpk.app.features.webview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_back_arrow_black
import org.jetbrains.compose.resources.painterResource

@Composable
expect fun PlatformWebView(
    modifier: Modifier, url: String,
    htmlContent: String? = null,
    isWebChat: Boolean? = false,
    cookies: Map<String, String>?
)

class WebViewScreen(
    private val url: String,
    private val htmlContent: String? = null,
    private val isWebChat: Boolean? = false,
    private val cookies: Map<String, String>? = emptyMap()
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
                    if (isWebChat == true)
                        TopAppBar(
                            title = { },
                            navigationIcon = {
                                Image(
                                    painter = painterResource(Res.drawable.ic_back_arrow_black),
                                    contentDescription = "Back",
                                    modifier = Modifier.clickable {
                                        navigator.pop()
                                    }.padding(start = 8.dp, top = 8.dp)
                                )

                            },
                            modifier = Modifier.height(56.dp)
                        )
                    PlatformWebView(
                        modifier = Modifier.fillMaxSize(),
                        url = url,
                        htmlContent = htmlContent,
                        isWebChat = isWebChat,
                        cookies = cookies
                    )
                }
            }
        }
    }
} 