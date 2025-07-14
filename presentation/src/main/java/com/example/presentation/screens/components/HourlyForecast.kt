package com.example.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.HourlyWeatherDisplayData

@Composable
fun HourlyForecast(
    hourly: List<HourlyWeatherDisplayData>,
    listState: LazyListState
) {
    val limitedHourly = hourly.take(10)
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(limitedHourly) { hour ->
            Column(
                modifier = Modifier.padding(end = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = hour.time)
                AsyncImage(
                    model = hour.iconUrl,
                    contentDescription = hour.description,
                    modifier = Modifier.size(48.dp)
                )
                Text(text = hour.temperature)
            }
        }
    }
}

