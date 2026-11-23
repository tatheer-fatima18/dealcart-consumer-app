package com.localpk.app.common_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.capitalizeFirst
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_close_normal
import dealcart.composeapp.generated.resources.warning_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class AlertDialogType {
    DEFAULT,
    WARNING
}

@Composable
fun CustomAlertDialog(
    isAddressDialog: Boolean,
    shouldShowDialog: MutableState<Boolean>,
    icon: DrawableResource?,
    title: String,
    onCancel: () -> Unit = {},
    message: String,
    confirmButton: String,
    cancelButton: String,
    dialogType: AlertDialogType? = AlertDialogType.DEFAULT,
    showCancelButton: Boolean = true,
    isDismissible: Boolean = true,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = { if (isDismissible) shouldShowDialog.value = false }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isDismissible) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, end = 10.dp)
                    ) {
                        IconButton(
                            onClick = {
                                shouldShowDialog.value = false
                                onCancel()
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(34.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_close_normal),
                                contentDescription = "Close",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(18.dp)
                            )
                        }
                    }
                }

                // Show icon based on dialog type or custom icon
                val displayIcon = when (dialogType) {
                    AlertDialogType.WARNING -> Res.drawable.warning_icon
                    AlertDialogType.DEFAULT, null -> icon
                }
                
                if (displayIcon != null) {
                    if (isAddressDialog) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(12.dp),
                        ) {
                            Image(
                                painter = painterResource(displayIcon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(8.dp)
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(displayIcon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = TextSubdued,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = TextSubdued,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (dialogType) {
                                AlertDialogType.WARNING -> Color(0xFFE53E3E) // Red color for warning
                                AlertDialogType.DEFAULT, null -> ButtonSurfaceBrand
                            }
                        )
                    ) {
                        Text(
                            text = confirmButton.capitalizeFirst(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    if (showCancelButton) {
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                shouldShowDialog.value = false
                                onCancel()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, SurfaceBackground),
                            colors = ButtonDefaults.buttonColors(SurfaceBackground)
                        ) {
                            Text(
                                text = cancelButton.capitalizeFirst(),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Medium,
                                    color = TextSubdued
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(26.dp))
            }
        }
    }
}