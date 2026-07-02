package com.example.jalsanchaytracker.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun formatTimestamp(timestamp: Long): String =
        displayFormat.format(Date(timestamp))
}
