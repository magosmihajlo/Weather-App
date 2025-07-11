package com.example.data.mapper

import com.example.domain.model.TimeFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TimeFormatter {

    fun formatTime(timestamp: Long, offsetSeconds: Int, format: TimeFormat): String {
        val formatter = DateTimeFormatter.ofPattern(
            if (format == TimeFormat.TWENTY_FOUR_HOUR) "HH:mm"
            else "h:mm a"
        )

        val zoneOffset = ZoneOffset.ofTotalSeconds(offsetSeconds)
        return Instant.ofEpochMilli(timestamp)
            .atOffset(zoneOffset)
            .format(formatter)
    }
}
