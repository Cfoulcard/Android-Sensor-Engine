package com.ui.composables

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.fragment.app.FragmentManager
import com.androidsensorengine.ui.theme.HomeScreenShapes
import com.androidsensorengine.ui.theme.pureWhite
import com.christianfoulcard.android.androidsensorengine.R
import com.ui.sensors.viewmodels.SoundSensorViewModel
import com.utils.CustomDialog.Companion.displayCustomDialog

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
fun InfoIcon(fragmentManager: FragmentManager, activity: Activity) {
    Image(
        painter = painterResource(R.drawable.ic_icon_info),
        contentDescription = "info",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(32.dp)
            .fillMaxSize().clickable { displayCustomDialog("Information", activity.getString(R.string.sound_desc_3), 0, true, fragmentManager, "info", activity) },
        alignment = Alignment.Center,
    )
}

@Composable
fun CentralGraphicSensorInfo(largeInfoString: String?, superScript: String?, description: String?, viewModel: SoundSensorViewModel) {

    val updatedString by viewModel.decibelLiveData.observeAsState()

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
                            updatedString?.let { append(it) }
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
fun SensorCometBackgroundForCentralGraphic() {
    Image(
        painter = painterResource(R.drawable.ic_comet_fade),
        contentDescription = "cometGradient",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxHeight(.5f)
            .fillMaxWidth(1f)
            .fillMaxSize()
            .blur(12.dp)
            .alpha(1f)
            .offset(y = (-150).dp),
        alignment = Alignment.Center,
    )
    Image(
        painter = painterResource(R.drawable.ic_comet_fade),
        contentDescription = "cometGradient",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth(1f)
            .fillMaxSize()
            .offset(y = (-150).dp),
        alignment = Alignment.Center,
    )
}

@Composable
fun FirstInfoLabelGroup(description: String?, value: String?, viewModel: SoundSensorViewModel) {
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
                FirstInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun FirstInfoLabelGroupValue(value: String?, viewModel: SoundSensorViewModel) {

    val updatedString by viewModel.averageDecibelLiveData.observeAsState()

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
                    updatedString?.let {
                        Text(
                            text = it,
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
}

@Composable
fun SecondInfoLabelGroup(description: String?, value: String?, viewModel: SoundSensorViewModel) {
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
                SecondInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun SecondInfoLabelGroupValue(value: String?, viewModel: SoundSensorViewModel) {
    Box(
        contentAlignment = Alignment.Center
    ) {

        val updatedString by viewModel.highestDecibelLiveData.observeAsState()

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
                    updatedString?.let {
                        Text(
                            text = it,
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
}

@Composable
fun ThirdInfoLabelGroup(description: String?, value: String?, viewModel: SoundSensorViewModel) {
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
                ThirdInfoLabelGroupValue(value, viewModel)
            }
        }
    }
}

@Composable
fun ThirdInfoLabelGroupValue(value: String?, viewModel: SoundSensorViewModel) {

    val updatedString by viewModel.lowestDecibelLiveData.observeAsState()

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
                    updatedString?.let {
                        Text(
                            text = it,
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
}

@Composable
fun PowerButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(R.drawable.ic_power_button),
            contentDescription = "cometGradient",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight(.43f)
                .fillMaxWidth(1f)
                .fillMaxSize()
                .alpha(1f)
                .blur(12.dp),
            alignment = Alignment.Center,
        )
        Image(
            painter = painterResource(R.drawable.ic_power_button),
            contentDescription = "cometGradient",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight(.4f)
                .fillMaxWidth(1f)
                .fillMaxSize(),
            alignment = Alignment.Center,
        )
        Image(
            painter = painterResource(R.drawable.ic_settings_icon),
            contentDescription = "cometGradient",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight(.35f)
                .fillMaxWidth(1f)
                .fillMaxSize()
                .offset(y = (-15).dp)
                .blur(12.dp),
            alignment = Alignment.CenterEnd,
        )
        Image(
            painter = painterResource(R.drawable.ic_settings_icon),
            contentDescription = "cometGradient",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight(.35f)
                .fillMaxWidth(1f)
                .fillMaxSize()
                .offset(y = (-15).dp),
            alignment = Alignment.CenterEnd,
        )

    }
}
