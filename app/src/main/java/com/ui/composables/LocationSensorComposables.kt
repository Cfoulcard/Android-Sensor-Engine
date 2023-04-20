package com.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
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
import com.ui.sensors.viewmodels.LocationSensorViewModel

@Composable
fun CentralLocationGraphicSensorInfo(largeInfoString: String?, superScript: String?, description: String?, viewModel: LocationSensorViewModel) {

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back_circle_dark),
            contentDescription = "backCircle",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(370.dp)
                .blur(8.dp)
                .alpha(0.75f),
            alignment = Alignment.Center,
        )
        Image(
            painter = painterResource(R.drawable.ic_back_circle),
            contentDescription = "backCircle",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(360.dp),
            alignment = Alignment.Center,
        )
        SensorCometBackgroundForCentralGraphic()
        Image(
            painter = painterResource(R.drawable.ic_top_circle_dark),
            contentDescription = "topCircle",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(260.dp)
                .blur(16.dp)
                .alpha(.90f),
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
                         //   append(viewModel.currentLux)
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
fun FirstLocationInfoLabelGroup(description: String?, value: String?, viewModel: LocationSensorViewModel) {
    Box(modifier = Modifier
        .fillMaxWidth().height(75.dp),
        contentAlignment = Alignment.Center) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 290.dp)
                .height(height = 44.dp)
                .fillMaxWidth(1f)
                .shadow(24.dp, clip = false),
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                FirstLocationInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun FirstLocationInfoLabelGroupValue(value: String?, viewModel: LocationSensorViewModel) {
  //  val updatedString by viewModel.averageLocationLiveData.observeAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 72.dp)
                .height(height = 44.dp)
                .shadow(24.dp, clip = false),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (value != null) {
//                    updatedString?.let {
//                        Text(
//                            text = it,
//                            color = Color(0xff292929),
//                            textAlign = TextAlign.Center,
//                            style = TextStyle(
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.Bold
//                            ),
//
//                            )
//                    }
                }
            }
        }
    }
}

@Composable
fun SecondLocationInfoLabelGroup(description: String?, value: String?, viewModel: LocationSensorViewModel) {
    Box(modifier = Modifier
        .fillMaxWidth().height(75.dp),
        contentAlignment = Alignment.Center) {
        Card(
            elevation = 24.dp,
            modifier = Modifier
                .width(width = 240.dp)
                .height(height = 44.dp)
                .fillMaxWidth(1f)
                .shadow(24.dp, clip = false),
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                SecondLocationInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun SecondLocationInfoLabelGroupValue(value: String?, viewModel: LocationSensorViewModel) {
 //  val updatedString by viewModel.highestLocationLiveData.observeAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {

        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 60.dp)
                .height(height = 44.dp)
                .shadow(24.dp, clip = false),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (value != null) {
//                    updatedString?.let {
//                        Text(
//                            text = it,
//                            color = Color(0xff292929),
//                            textAlign = TextAlign.Center,
//                            style = TextStyle(
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.Bold
//                            ),
//
//                            )
//                    }
                }
            }
        }
    }
}

@Composable
fun ThirdLocationInfoLabelGroup(description: String?, value: String?, viewModel: LocationSensorViewModel) {
    Box(modifier = Modifier
        .fillMaxWidth().height(75.dp),
        contentAlignment = Alignment.Center) {
        Card(
            elevation = 24.dp,
            modifier = Modifier
                .width(width = 190.dp)
                .height(height = 44.dp)
                .fillMaxWidth(1f)
                .shadow(24.dp, clip = false),
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                ThirdLocationInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun ThirdLocationInfoLabelGroupValue(value: String?, viewModel: LocationSensorViewModel) {
//    val updatedString by viewModel.lowestLocationLiveData.observeAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 47.dp)
                .height(height = 44.dp)
                .shadow(24.dp, clip = false),
            shape = HomeScreenShapes.small,
            backgroundColor = pureWhite,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (value != null) {
//                    updatedString?.let {
//                        Text(
//                            text = it,
//                            color = Color(0xff292929),
//                            textAlign = TextAlign.Center,
//                            style = TextStyle(
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.Bold
//                            ),
//                        )
//                    }
                }
            }
        }
    }
}