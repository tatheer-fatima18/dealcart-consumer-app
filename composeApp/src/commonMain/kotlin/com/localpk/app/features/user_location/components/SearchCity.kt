package com.localpk.app.features.user_location.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.CartAmountColor90
import com.localpk.app.theming.ColorPrimary60
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextGray60
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchCity(searchText: String, onChangeText: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    val KeyBoardController = LocalSoftwareKeyboardController.current
    Box(
        Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x30576AFF),
                ambientColor = Color(0x30576AFF)
            )
            .background(color = Color.White)
    ) {
        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = TextGray60,
                focusedBorderColor = ColorPrimary60,
            ),
            value = searchText,
            maxLines = 1,
            onValueChange = {
                onChangeText(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            placeholder = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp),
                    text = "Search City...",
                    color = CartAmountColor90,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                )
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = null,
                    tint = TextGray60
                )
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    // When user hits the search button, perform search
                    KeyBoardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            textStyle = TextStyle(
                color = CartAmountColor90,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}