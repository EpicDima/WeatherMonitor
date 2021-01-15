package by.resliv.weathermonitor.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author EpicDima
 */
private val MINUTES_HOUR_FORMAT = SimpleDateFormat("HH:mm", Locale.US)
private val DAY_MONTH_FORMAT = SimpleDateFormat("MMMM dd", Locale.US)

fun toTime(timestamp: Long): String {
    return MINUTES_HOUR_FORMAT.format(Date(timestamp * 1000L))
}

fun toDate(timestamp: Long): String {
    return DAY_MONTH_FORMAT.format(Date(timestamp * 1000L))
}