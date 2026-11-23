package com.localpk.app.features.bottomsheet.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.bottomsheet.components.GeneralBottomSheetContent
import com.localpk.app.features.bottomsheet.data.GeneralBottomSheetUiData
import com.localpk.app.features.orders.data.GlobalEvents

class GeneralBottomSheetScreen : Screen {

    @Composable
    override fun Content() {
        val argsData = GlobalEvents.argsGeneralBottomSheetScreen.value
        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
        ) {
            if (argsData != null) {
                GeneralBottomSheetContent(argsData)
                GlobalEvents.updateGeneralBottomSheetScreen(null)
            }
        }
    }
}