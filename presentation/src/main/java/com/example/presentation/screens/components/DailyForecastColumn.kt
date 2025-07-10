package com.example.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
            val date = remember(day.dateEpoch) {
                Instant.ofEpochSecond(day.dateEpoch)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("EEE"))
            }

            val min = day.minTemp.toInt()
            val max = day.maxTemp.toInt()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text(
                    text = date,
                    modifier = Modifier.width(40.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${day.iconCode}@2x.png",
                    contentDescription = day.description,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.width(75.dp))

                Text(
                    text = "$min°",
                    modifier = Modifier.width(40.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width((max * 3).dp.coerceAtMost(180.dp))
                        .padding(horizontal = 4.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF2196F3),
                                    Color(0xFFFF9800)
                                )
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )

                Text(
                    text = "$max°",
                    modifier = Modifier
                        .width(40.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
