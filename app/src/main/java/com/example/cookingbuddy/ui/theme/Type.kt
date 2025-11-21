package com.example.cookingbuddy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cookingbuddy.R

val Outfit = FontFamily(
    Font(R.font.outfit_thin, FontWeight.Thin),
    Font(R.font.outfit_extralight, FontWeight.ExtraLight),
    Font(R.font.outfit_light, FontWeight.Light),
    Font(R.font.outfit_regular, FontWeight.Normal),
    Font(R.font.outfit_medium, FontWeight.Medium),
    Font(R.font.outfit_semibold, FontWeight.SemiBold),
    Font(R.font.outfit_bold, FontWeight.Bold),
    Font(R.font.outfit_extrabold, FontWeight.ExtraBold),
    Font(R.font.outfit_black, FontWeight.Black),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun headlineBigTextStyle(color: Color = Color.Black) = TextStyle(
    fontFamily = Outfit,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    color = color
)

@Composable
fun headlineTextStyle(color: Color = Color.Black) = TextStyle(
    fontFamily = Outfit,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    color = color
)

@Composable
fun bodyTextStyle(color: Color = Color.Black) = TextStyle(
    fontFamily = Outfit,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = color
)

@Composable
fun smallTextStyle(color: Color = Color.Black) = TextStyle(
    fontFamily = Outfit,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    color = color
)

@Composable
fun smallXTextStyle(color: Color = Color.Black) = TextStyle(
    fontFamily = Outfit,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    color = color
)

@Composable
fun TextStyle.lightEmphasize() = this.copy(fontWeight = FontWeight.SemiBold)
