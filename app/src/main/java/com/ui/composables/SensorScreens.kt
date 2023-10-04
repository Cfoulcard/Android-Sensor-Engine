package com.ui.composables

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.christianfoulcard.android.androidsensorengine.R
import com.utils.CustomDialog.Companion.displayCustomDialog

@Composable
fun DisplaySensorTitle(text: String) {
    AutosizedTextTitle(text)
}

@Composable
fun InfoIcon(fragmentManager: FragmentManager, activity: Activity, description: Int) {
    Image(
        painter = painterResource(R.drawable.ic_icon_info),
        contentDescription = "info",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(32.dp)
            .fillMaxSize().clickable {
                displayCustomDialog(
                    activity.getString(R.string.information),
                    activity.getString(description),
                    0,
                    true,
                    fragmentManager,
                    "info", activity
                )},
        alignment = Alignment.Center,
    )
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
