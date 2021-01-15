package by.resliv.weathermonitor.settings

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * @author EpicDima
 */
const val APP_PREFERENCES = "app_preferences"

const val CHOOSED_KEY = "choosed"
const val TEMPERATURE_KEY = "temperature"
const val WIND_KEY = "wind"
const val PRECIP_KEY = "precip"
const val PRESSURE_KEY = "pressure"
const val VISIBILITY_KEY = "visibility"
const val SCHEDULE_KEY = "schedule"

const val TEMPERATURE_C = "c"
const val TEMPERATURE_F = "f"

const val WIND_KPH = "kph"
const val WIND_MPH = "mph"

const val PRECIP_MM = "mm"
const val PRECIP_IN = "in"

const val PRESSURE_MM = "mm"
const val PRESSURE_IN = "in"

const val VISIBILITY_KM = "km"
const val VISIBILITY_MI = "mi"

// minutes
const val SCHEDULE_15 = "15"
const val SCHEDULE_30 = "30"
const val SCHEDULE_60 = "60"
const val SCHEDULE_120 = "120"  // 2 hours
const val SCHEDULE_240 = "240"  // 4 hours
const val SCHEDULE_480 = "480"  // 8 hours
const val SCHEDULE_720 = "720"  // 12 hours

fun <T> SharedPreferences.set(key: String, value: T) {
    edit {
        when (value) {
            is Int -> {
                putInt(key, value)
            }
            is String -> {
                putString(key, value)
            }
        }
    }
}

fun SharedPreferences.getString(key: String): String {
    return when (key) {
        SCHEDULE_KEY -> getString(key, SCHEDULE_240)!!
        TEMPERATURE_KEY -> getString(key, TEMPERATURE_C)!!
        WIND_KEY -> getString(key, WIND_KPH)!!
        PRECIP_KEY -> getString(key, PRECIP_MM)!!
        PRESSURE_KEY -> getString(key, PRESSURE_MM)!!
        VISIBILITY_KEY -> getString(key, VISIBILITY_KM)!!
        else -> ""
    }
}

fun SharedPreferences.getInt(key: String): Int {
    return when (key) {
        CHOOSED_KEY -> getInt(key, 0)
        else -> 0
    }
}

fun getStringValues(key: String): Array<String> {
    return when (key) {
        SCHEDULE_KEY -> arrayOf(
            SCHEDULE_15,
            SCHEDULE_30,
            SCHEDULE_60,
            SCHEDULE_120,
            SCHEDULE_240,
            SCHEDULE_480,
            SCHEDULE_720
        )
        TEMPERATURE_KEY -> arrayOf(TEMPERATURE_C, TEMPERATURE_F)
        WIND_KEY -> arrayOf(WIND_KPH, WIND_MPH)
        PRECIP_KEY -> arrayOf(PRECIP_MM, PRECIP_IN)
        PRESSURE_KEY -> arrayOf(PRESSURE_MM, PRESSURE_IN)
        VISIBILITY_KEY -> arrayOf(VISIBILITY_KM, VISIBILITY_MI)
        else -> arrayOf()
    }
}

data class MeasurementUnits(
    val temperatureUnit: String,
    val windUnit: String,
    val precipUnit: String,
    val pressureUnit: String,
    val visibilityUnit: String,
)

fun SharedPreferences.getMeasurementUnits(): MeasurementUnits {
    return MeasurementUnits(
        temperatureUnit = getString(TEMPERATURE_KEY),
        windUnit = getString(WIND_KEY),
        precipUnit = getString(PRECIP_KEY),
        pressureUnit = getString(PRESSURE_KEY),
        visibilityUnit = getString(VISIBILITY_KEY),
    )
}