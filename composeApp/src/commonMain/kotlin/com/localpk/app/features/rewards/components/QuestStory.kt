package com.localpk.app.features.rewards.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily

@Composable
fun QuestStory(
    caption: String,
    onClick: () -> Unit,
    onEventClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       /* AndroidView(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .clickable {
                    onClick()
                    onEventClick()
                },
            factory = { context ->
                ImageView(context).apply {
                    setImageResource(R.drawable.profile_card)
                }
            }
        )*/
        Text(
            text = caption,
            modifier = Modifier.padding(top = 5.dp),
            style = TextStyle(fontSize = 10.sp),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Light,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}