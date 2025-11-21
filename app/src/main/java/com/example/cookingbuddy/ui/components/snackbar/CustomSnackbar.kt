package com.example.cookingbuddy.ui.components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cookingbuddy.R
import com.example.cookingbuddy.ui.theme.CookingBuddyTheme
import com.example.cookingbuddy.ui.theme.bodyTextStyle
import com.example.cookingbuddy.ui.theme.smallXTextStyle

@Composable
fun CustomSnackBar(
    snackBarType: SnackBarType,
    title: String? = null,
    message: String? = null,
    dismiss: (() -> Unit)? = null,
) {
    val primaryColor = when (snackBarType) {
        SnackBarType.Error -> MaterialTheme.colorScheme.error
    }
    val shape = RoundedCornerShape(
        topStart = 4.dp,
        topEnd = 4.dp,
        bottomStart = 4.dp,
        bottomEnd = 12.dp,
    )

    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                spotColor = primaryColor.copy(alpha = 0.4f),
                ambientColor = primaryColor.copy(alpha = 0.4f),
            )
            .background(MaterialTheme.colorScheme.surface, shape = shape)
            .border(
                width = 1.dp,
                color = primaryColor,
                shape = shape,
            )
            .padding(8.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_ph_warning),
            contentDescription = null,
            tint = primaryColor,
        )

        Column(
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
                .weight(1f)
                .align(Alignment.CenterVertically),
        ) {
            if (title != null) {
                Text(
                    text = title,
                    style = bodyTextStyle(),
                )

                if (message != null) {
                    Text(
                        text = message,
                        style = smallXTextStyle(),
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            } else if (message != null) {
                Text(
                    text = message,
                    style = bodyTextStyle(),
                )
            }
        }

        val interactionSource = remember { MutableInteractionSource() }
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
                .align(Alignment.CenterVertically)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) {
                    dismiss?.invoke()
                },
        )
    }
}

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
private fun CustomSnackBarPreview() {
    CookingBuddyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            CustomSnackBar(
                snackBarType = SnackBarType.Error,
                title = "Ups, ceva nu a funcționat!",
                message = "Te rugăm să verifici datele introduse sau încearcă mai târziu.",
            )
        }
    }
}