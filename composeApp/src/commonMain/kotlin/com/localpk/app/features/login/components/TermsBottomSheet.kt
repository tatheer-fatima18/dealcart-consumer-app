package com.localpk.app.features.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.localpk.app.base.getScreenHeightDp
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.close_main
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsBottomSheet(
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    val maxSheetHeight = getScreenHeightDp().dp * 0.7f
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
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.close_main),
                    modifier = Modifier.clickable { onClose() },
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier.fillMaxWidth().height(maxSheetHeight)
                    .background(
                        Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ).padding(vertical = 20.dp)
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsBottomSheet(
    onClose: () -> Unit,
    content: @Composable () -> Unit,
    heightModifier: Modifier = Modifier.wrapContentHeight()
) {
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
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.close_main),
                    modifier = Modifier.clickable { onClose() },
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier.fillMaxWidth().then(heightModifier)
                    .background(
                        Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ).padding(vertical = 20.dp)
            ) {
                content()
            }
        }
    }
}