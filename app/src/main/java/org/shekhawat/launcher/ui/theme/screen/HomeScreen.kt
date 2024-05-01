package org.shekhawat.launcher.ui.theme.screen

import android.content.Intent
import android.os.BatteryManager
import android.provider.MediaStore
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.shekhawat.laucher.R
import java.time.LocalDateTime

@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val rightSign = ""
    val leftSign = ""
    val quoteSign = "" // "Don’t count the days,\nMake the days count."
    // get battery percentage
    val bm =
        LocalContext.current.getSystemService(android.content.Context.BATTERY_SERVICE) as BatteryManager
    var isCharging by remember {
        mutableStateOf(bm.isCharging)
    }
    var batteryPercentage by remember {
        mutableIntStateOf(bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY))
    }
    var time by remember {
        mutableStateOf(LocalDateTime.now())
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            batteryPercentage = withContext(Dispatchers.Default) {
                // get the battery percentage
                bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            }
            time = withContext(Dispatchers.Default) {
                LocalDateTime.now()
            }
            isCharging = withContext(Dispatchers.Default) {
                bm.isCharging
            }
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimary)) {
                        append("STAY\n")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFFFF9800))) {
                        append(" FOCUSED\n")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimary)) {
                        append("\uD83E\uDD10")
                    }
                },
                textAlign = TextAlign.Center,
                lineHeight = 50.sp,
                style = MaterialTheme.typography.displaySmall
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            text = quoteSign,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp),
            text = leftSign,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(18.dp),
            text = rightSign,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { batteryPercentage / 100.0f },
                modifier = Modifier
                    .size(225.dp)
                    .align(Alignment.TopCenter)
                    .animateContentSize(),
                color = MaterialTheme.colorScheme.onPrimary,
                trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
            )
            TimeScreen(batteryPercentage, isCharging, time)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        // open the dialer
                        context.startActivity(Intent(Intent.ACTION_DIAL))
                    },
                painter = painterResource(id = R.drawable.telephone),
                contentDescription = "Phone"
            )
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        // open the camera
                        context.startActivity(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    },
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "Camera"
            )
        }
    }
}

@Composable
fun TimeScreen(batteryPercentage: Int, isCharging: Boolean, time: LocalDateTime) {
    val context = LocalContext.current
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable {
                    // open the battery settings
                    context.startActivity(Intent(Intent.ACTION_POWER_USAGE_SUMMARY))
                },
            text = "${batteryPercentage}%"
        )
        if (isCharging) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Charging ")
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.battery),
                    contentDescription = "Charging"
                )
            }
        }
        Text(
            // show in two digit format
            text = String.format("%02d:%02d", time.hour, time.minute),
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    // open the clock
                    context.startActivity(Intent(Intent.ACTION_QUICK_CLOCK))
                },
            style = MaterialTheme.typography.displayLarge,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "${time.month}, ${time.dayOfMonth} ${time.dayOfWeek.toString().substring(0, 3)}"
        )
    }
}