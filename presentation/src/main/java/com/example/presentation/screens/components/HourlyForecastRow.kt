package com.example.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.HourlyWeather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HourlyForecastRow(hourly: List<HourlyWeather>) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(hourly) { hour ->
            Column(
                modifier = Modifier
                    .padding(end = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val hourStr = remember(hour.timeEpoch) {
                    Instant.ofEpochSecond(hour.timeEpoch)
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("HH:mm"))
                }
                Text(text = hourStr)
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${hour.iconCode}@2x.png",
                    contentDescription = hour.description,
                    modifier = Modifier.size(48.dp)
                )
                Text(text = "${hour.temperature}°C")
            }
        }
    }
}
