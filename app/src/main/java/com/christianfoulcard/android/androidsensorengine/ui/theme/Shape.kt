package com.christianfoulcard.android.androidsensorengine.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp),
)

val HomeScreenShapes = Shapes(
    small = RoundedCornerShape(25.dp),
    medium = RoundedCornerShape(25.dp, 25.dp, 0.dp, 0.dp),
    large = RoundedCornerShape(0.dp, 0.dp, 100.dp, 100.dp)
)

val BackgroundShapes = Shapes(
    small = RoundedCornerShape(0.dp, 0.dp, 300.dp, 300.dp)
)