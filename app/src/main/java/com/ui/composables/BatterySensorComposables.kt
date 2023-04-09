package com.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.ui.sensors.viewmodels.BatterySensorViewModel

@Composable
fun CentralBatteryGraphicSensorInfo(largeInfoString: String?, superScript: String?, description: String?, viewModel: BatterySensorViewModel) {

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
                            append(viewModel.batteryTemperature.toString())
                        }
                    }
                    withStyle(style = SpanStyle(
                        fontSize = 22.sp,
                        baselineShift = BaselineShift(+0.7f)
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
                    text = viewModel.batteryTemperatureName,
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
fun FirstBatteryInfoLabelGroup(description: String?, value: String?, viewModel: BatterySensorViewModel) {
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
                FirstBatteryInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun FirstBatteryInfoLabelGroupValue(value: String?, viewModel: BatterySensorViewModel) {
    val updatedString by viewModel.batteryPercentageLiveData.observeAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 145.dp)
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
                        Text(
                            text = "${updatedString.toString()} %",
                            color = Color(0xff292929),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                        )
                }
            }
        }
    }
}

@Composable
fun SecondBatteryInfoLabelGroup(description: String?, value: String?, viewModel: BatterySensorViewModel) {
    Box(modifier = Modifier
        .fillMaxWidth().height(75.dp),
        contentAlignment = Alignment.Center) {
        Card(
            elevation = 24.dp,
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
                SecondBatteryInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun SecondBatteryInfoLabelGroupValue(value: String?, viewModel: BatterySensorViewModel) {
    val updatedString by viewModel.batteryVoltageLiveData.observeAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {

        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 145.dp)
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
                        Text(
                            text = "${updatedString.toString()} mV",
                            color = Color(0xff292929),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                        )
                }
            }
        }
    }
}

@Composable
fun ThirdBatteryInfoLabelGroup(description: String?, value: String?, viewModel: BatterySensorViewModel) {
    Box(modifier = Modifier
        .fillMaxWidth().height(75.dp),
        contentAlignment = Alignment.Center) {
        Card(
            elevation = 24.dp,
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
                ThirdBatteryInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun ThirdBatteryInfoLabelGroupValue(value: String?, viewModel: BatterySensorViewModel) {
    val updatedString by viewModel.batteryHealthLiveData.observeAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .width(width = 145.dp)
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
                        Text(
                            text = updatedString.toString(),
                            color = Color(0xff292929),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                        )
                }
            }
        }
    }
}