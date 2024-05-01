package org.shekhawat.launcher.ui.theme.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime

fun getHour(currentTime: LocalDateTime): String {
    return String.format("%02d", currentTime.hour)
}

fun getMinute(currentTime: LocalDateTime): String {
    return String.format("%02d", currentTime.minute)
}

@Composable
fun PomodoroScreen() {

    val currentTime = LocalDateTime.now()
    val hour = getHour(currentTime)
    val minute = getMinute(currentTime)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeWidget(modifier = Modifier.weight(1f), text = hour)
        TimeWidget(modifier = Modifier.weight(1f), text = minute)
    }
}

@Composable
fun TimeWidget(modifier: Modifier, text: String) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 250.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 10.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
