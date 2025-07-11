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
import com.example.domain.model.DailyWeatherDisplayData

@Composable
fun DailyForecastColumn(daily: List<DailyWeatherDisplayData>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        daily.forEach { day ->
            val minTemp = remember(day.minTemperature) {
                day.minTemperature.filter { it.isDigit() || it == '.' || it == '-' }.toFloatOrNull() ?: 0f
            }
            val maxTemp = remember(day.maxTemperature) {
                day.maxTemperature.filter { it.isDigit() || it == '.' || it == '-' }.toFloatOrNull() ?: 0f
            }
            val rangeWidth = (maxTemp - minTemp).coerceAtLeast(0f) * 3f

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = day.dayName, modifier = Modifier.width(40.dp))

                AsyncImage(
                    model = day.iconUrl,
                    contentDescription = day.description,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.width(55.dp))

                Text(
                    text = day.minTemperature,
                    modifier = Modifier.width(60.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(rangeWidth.dp.coerceAtMost(180.dp))
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
                    text = day.maxTemperature,
                    modifier = Modifier
                        .width(60.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

