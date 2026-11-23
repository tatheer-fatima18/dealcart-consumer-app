package com.localpk.app.features.address.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dealcart.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.localpk.app.features.address.data.AddAddressRequest
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.add_second_phone_number
import dealcart.composeapp.generated.resources.choose_from_phone_book
import dealcart.composeapp.generated.resources.complete_phone
import dealcart.composeapp.generated.resources.confirm
import dealcart.composeapp.generated.resources.primary_phone_number
import dealcart.composeapp.generated.resources.secondary_phone_title
import dealcart.composeapp.generated.resources.skip_for_now
import org.koin.compose.koinInject

@Composable
fun AddPhoneNumber(
    args: AddAddressRequest,
    onPhoneNumberChange: (String) -> Unit = {},
    addButton: Boolean,
    onConfirmClick: (String) -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    val sharedPreferencesManager = koinInject<SharedPreferencesManager>()

    val phoneNumber = remember { mutableStateOf(args.phoneNumber.default()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .imePadding()
    ) {
        Text(
            text = stringResource(Res.string.complete_phone),
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFontFamily(),
            color = ColorPrimaryText,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = stringResource(Res.string.primary_phone_number),
            color = TextSubdued,
            fontFamily = PoppinsFontFamily(),
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 2.dp)
        )

        DisabledPhoneField(text = sharedPreferencesManager.getPhoneNumber())

        Spacer(Modifier.height(10.dp))

        Text(
            text = stringResource(Res.string.secondary_phone_title),
            color = TextSubdued,
            fontFamily = PoppinsFontFamily(),
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 2.dp)
        )

        EditablePhoneField(
            value = phoneNumber.value,
            onValueChange = {
                phoneNumber.value = it
                onPhoneNumberChange(it)
            }
        )

        Spacer(Modifier.height(12.dp))

//        TextButton(
//            onClick = {}, modifier = Modifier.align(Alignment.Start)
//            // viewModel::openContacts
//        ) {
//            Text(
//                text = stringResource(Res.string.choose_from_phone_book),
//                fontSize = 14.sp,
//                fontFamily = PoppinsFontFamily(),
//                fontWeight = FontWeight.SemiBold,
//                color = TextPrimary
//
//            )
//        }
//
//        Spacer(Modifier.height(5.dp))


        Button(
            onClick = {
                onConfirmClick(phoneNumber.value)
                args.listener.invoke()
            },
            enabled = addButton,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.buttonColors(
                ColorPrimary
            )
        ) {
            Text(
                text = if (args.phoneNumber.isNullOrBlank())
                    stringResource(Res.string.add_second_phone_number)
                else
                    stringResource(Res.string.confirm)
            )
        }

        TextButton(onClick = {
            onSkipClick()
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = stringResource(Res.string.skip_for_now),
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = TextSubduedHalf
            )
        }
    }
}
