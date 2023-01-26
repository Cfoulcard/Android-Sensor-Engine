package com.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidsensorengine.ui.theme.HomeScreenShapes
import com.androidsensorengine.ui.theme.pureWhite
import com.christianfoulcard.android.androidsensorengine.R

@Composable
fun DisplaySensorTitle(text: String) {
    Text(
        text = text,
        fontSize = 36.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 32.dp),
        style = MaterialTheme.typography.h1
    )
}

@Composable
fun InfoIcon() {
    Image(
        painter = painterResource(R.drawable.ic_icon_info),
        contentDescription = "info",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(32.dp)
            .fillMaxSize(),
        alignment = Alignment.Center,
    )
}

@Composable
fun CentralGraphicSensorInfo(largeInfoString: String?, superScript: String?, description: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back_circle_dark),
            contentDescription = "backCircle",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(370.dp).blur(8.dp).alpha(0.75f),
            alignment = Alignment.Center,
        )
        Image(
            painter = painterResource(R.drawable.ic_back_circle),
            contentDescription = "backCircle",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(360.dp),
            alignment = Alignment.Center,
        )
        Image(
            painter = painterResource(R.drawable.ic_top_circle_dark),
            contentDescription = "topCircle",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(260.dp).blur(16.dp).alpha(.90f),
            alignment = Alignment.Center,
        )
        Image(
            painter = painterResource(R.drawable.ic_top_circle),
            contentDescription = "topCircle",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(250.dp),
            alignment = Alignment.Center,
        )
        Column(
            modifier = Modifier.absoluteOffset(4.dp, (-4).dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontSize = 48.sp)
                    ) {
                        if (largeInfoString != null) {
                            append(largeInfoString)
                        }
                    }
                    withStyle(style = SpanStyle(
                        fontSize = 14.sp,
                        baselineShift = BaselineShift(+1f)
                    )
                    ) {
                        if (superScript != null) {
                            append(superScript)
                        }
                    }
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,

                )
            if (description != null) {
                Text(
                    text = description,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.absoluteOffset(y = (-26).dp)
                )
            }
        }
    }

}

@Composable
fun FirstInfoLabelGroup(description: String?, value: String?) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .offset(y = (-60).dp),
        contentAlignment = Alignment.Center) {
        Card(
            elevation = 24.dp,
            modifier = Modifier
                .width(width = 272.dp)
                .height(height = 44.dp)
                .fillMaxWidth(1f),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                if (description != null) {
                    Text(
                        text = description,
                        color = Color(0xff292929),
                        textAlign = TextAlign.Left,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                FirstInfoLabelGroupValue(value)
            }
        }
    }
}

@Composable
fun FirstInfoLabelGroupValue(value: String?) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 72.dp)
                .height(height = 44.dp),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (value != null) {
                    Text(
                        text = value,
                        color = Color(0xff292929),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),

                        )
                }
            }
        }
    }
}

@Composable
fun SecondInfoLabelGroup(description: String?, value: String?) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .offset(y = (-40).dp),
        contentAlignment = Alignment.Center) {
        Card(
            elevation = 24.dp,
            modifier = Modifier
                .width(width = 228.dp)
                .height(height = 36.dp)
                .fillMaxWidth(1f),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                if (description != null) {
                    Text(
                        text = description,
                        color = Color(0xff292929),
                        textAlign = TextAlign.Left,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                SecondInfoLabelGroupValue(value)
            }
        }
    }
}

@Composable
fun SecondInfoLabelGroupValue(value: String?) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 60.dp)
                .height(height = 36.dp),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (value != null) {
                    Text(
                        text = value,
                        color = Color(0xff292929),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),

                        )
                }
            }
        }
    }
}

@Composable
fun ThirdInfoLabelGroup(description: String?, value: String?) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .offset(y = (-20).dp),
        contentAlignment = Alignment.Center) {
        Card(
            elevation = 24.dp,
            modifier = Modifier
                .width(width = 176.dp)
                .height(height = 36.dp)
                .fillMaxWidth(1f),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                if (description != null) {
                    Text(
                        text = description,
                        color = Color(0xff292929),
                        textAlign = TextAlign.Left,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                ThirdInfoLabelGroupValue(value)
            }
        }
    }
}

@Composable
fun ThirdInfoLabelGroupValue(value: String?) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 47.dp)
                .height(height = 36.dp),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (value != null) {
                    Text(
                        text = value,
                        color = Color(0xff292929),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),

                        )
                }
            }
        }
    }
}

