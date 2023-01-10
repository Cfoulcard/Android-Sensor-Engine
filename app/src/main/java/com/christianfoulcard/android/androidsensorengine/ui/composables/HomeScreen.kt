package com.christianfoulcard.android.androidsensorengine.ui.composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christianfoulcard.android.androidsensorengine.R

    @Composable
    fun SensorDiagnosisRow(text: String, drawable: Int) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(0.6f),
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.h1,
            )
            Image(
                painter = painterResource(drawable),
                contentDescription = "avatar",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(16.dp).align(Alignment.CenterVertically),
                alignment = Alignment.Center,
            )
        }
    }

