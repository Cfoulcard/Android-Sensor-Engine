package com.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AutosizedTextTitle(text: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current.density
    val screenWidthPx = screenWidth.value * density
    val maxTextSize = 36f
    val textSize = (screenWidthPx / text.length).coerceIn(1f, maxTextSize).sp

        Text(
            modifier = Modifier.padding(top = 64.dp),
            text = text,
            style = TextStyle(fontSize = textSize),
            textAlign = TextAlign.Center
        )

}