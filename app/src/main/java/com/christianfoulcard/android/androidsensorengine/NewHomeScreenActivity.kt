package com.christianfoulcard.android.androidsensorengine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christianfoulcard.android.androidsensorengine.ui.theme.*

class NewHomeScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSensorEngineTheme {
                MainGradientBackground()
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(0.dp)
                ) {
                    HomeScreenSensorGrid()
                    SensorDiagnosisView()
                }
            }
        }
    }

    @Composable
    fun MainGradientBackground() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            skyBabyBlue,
                            cloudPink
                        )
                    )
                )
        ) {
        }
    }

    @Composable
    fun HomeScreenSensorGrid() {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            modifier = Modifier.padding(vertical = 72.dp)
        ) {
            val data = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")

            items(data) { item ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(100.dp),
                    backgroundColor = pureWhite,
                    shape = HomeScreenShapes.small,
                    elevation = 12.dp
                ) {
                    Text(
                        text = item,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun SensorDiagnosisView() {
        Surface(
            modifier = Modifier.padding(24.dp).size(600.dp),
            shape = HomeScreenShapes.small,
            color = pureWhite
        ) {

        }
    }


    @Composable
    fun Greeting(name: String) {
        Card {
            Text(
                text = "I love you $name!",
                color = Color.DarkGray,
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.W700,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AndroidSensorEngineTheme {
            MainGradientBackground()
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(0.dp)
            ) {
                HomeScreenSensorGrid()
                SensorDiagnosisView()
            }
        }
    }
}