package com.example.data.mapper

import com.example.domain.model.TimeFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimeFormatter {

    fun formatTime(timestampMillis: Long, format: TimeFormat): String {
        val pattern = when (format) {
            TimeFormat.TWELVE_HOUR -> "h:mm a"
            TimeFormat.TWENTY_FOUR_HOUR -> "HH:mm"
        }

        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestampMillis))
    }
}
