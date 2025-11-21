package com.example.cookingbuddy.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cookingbuddy.ui.theme.CookingBuddyTheme
import com.example.cookingbuddy.ui.theme.bodyTextStyle

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier.wrapContentSize(),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            text = text,
            style = bodyTextStyle(),
            color = Color.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview() {
    CookingBuddyTheme {
        PrimaryButton(modifier = Modifier.padding(16.dp), text = "Primary Button", onClick = {})
    }
}