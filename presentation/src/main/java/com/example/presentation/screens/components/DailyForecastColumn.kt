package com.example.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.DailyWeather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun DailyForecastColumn(daily: List<DailyWeather>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        daily.forEach { day ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val date = remember(day.dateEpoch) {
                    Instant.ofEpochSecond(day.dateEpoch)
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("EEE, dd"))
                }
                Text(modifier = Modifier.weight(1f), text = date)
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${day.iconCode}@2x.png",
                    contentDescription = day.description,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${day.minTemp.toInt()}°/${day.maxTemp.toInt()}°")
            }
        }
    }
}
