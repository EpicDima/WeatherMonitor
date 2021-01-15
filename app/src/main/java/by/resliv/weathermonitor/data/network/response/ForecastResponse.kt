package by.resliv.weathermonitor.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * @author EpicDima
 */
data class ForecastResponse(
    val location: Location,
    val current: CurrentForecast,
    val forecast: InnerForecast
) {
    fun toLocationModel(): by.resliv.weathermonitor.model.Location {
        return location.toDataModel()
    }

    fun toCurrentForecastModel(): by.resliv.weathermonitor.model.CurrentForecast {
        return current.toDataModel()
    }

    fun toDailyAndHourlyForecastModel(): Pair<List<by.resliv.weathermonitor.model.DailyForecast>,
            List<List<by.resliv.weathermonitor.model.HourlyForecast>>> {
        return Pair(
            forecast.forecastday.map { it.toDataModel() }.toList(),
            forecast.forecastday.map {
                it.hour.map { hourlyForecast -> hourlyForecast.toDataModel() }.toList()
            }.toList()
        )
    }
}

data class Location(
    val name: String,
    val lat: Double,
    val lon: Double,
) {
    fun toDataModel(): by.resliv.weathermonitor.model.Location {
        return by.resliv.weathermonitor.model.Location(name = name, latitude = lat, longitude = lon)
    }
}

data class Condition(
    val text: String,
    val icon: String
)

data class CurrentForecast(
    @SerializedName("temp_c")
    val tempC: Float,
    @SerializedName("temp_f")
    val tempF: Float,
    val condition: Condition,
    @SerializedName("wind_mph")
    val windMph: Float,
    @SerializedName("wind_kph")
    val windKph: Float,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("pressure_mb")
    val pressureMb: Float,
    @SerializedName("pressure_in")
    val pressureIn: Float,
    @SerializedName("precip_mm")
    val precipMm: Float,
    @SerializedName("precip_in")
    val precipIn: Float,
    val humidity: Float,
    val cloud: Float,
    @SerializedName("feelslike_c")
    val feelslikeC: Float,
    @SerializedName("feelslike_f")
    val feelslikeF: Float,
    @SerializedName("vis_km")
    val visKm: Float,
    @SerializedName("vis_miles")
    val visMiles: Float,
    val uv: Float,
) {
    fun toDataModel(): by.resliv.weathermonitor.model.CurrentForecast {
        return by.resliv.weathermonitor.model.CurrentForecast(
            tempC = tempC,
            tempF = tempF,
            text = condition.text,
            icon = condition.icon,
            windMph = windMph,
            windKph = windKph,
            windDir = windDir,
            pressureMb = pressureMb,
            pressureIn = pressureIn,
            precipMm = precipMm,
            precipIn = precipIn,
            humidity = humidity,
            cloud = cloud,
            feelslikeC = feelslikeC,
            feelslikeF = feelslikeF,
            visKm = visKm,
            visMiles = visMiles,
            uv = uv
        )
    }
}

data class InnerForecast(
    val forecastday: List<DailyForecast>
)

data class DailyForecast(
    @SerializedName("date_epoch")
    val dateEpoch: Long,
    val day: Day,
    val hour: List<HourlyForecast>
) {
    fun toDataModel(): by.resliv.weathermonitor.model.DailyForecast {
        return by.resliv.weathermonitor.model.DailyForecast(
            dateEpoch = dateEpoch,
            maxTempC = day.maxtempC,
            maxTempF = day.maxtempF,
            minTempC = day.mintempC,
            minTempF = day.mintempF,
            text = day.condition.text,
            icon = day.condition.icon
        )
    }
}

data class Day(
    @SerializedName("maxtemp_c")
    val maxtempC: Float,
    @SerializedName("maxtemp_f")
    val maxtempF: Float,
    @SerializedName("mintemp_c")
    val mintempC: Float,
    @SerializedName("mintemp_f")
    val mintempF: Float,
    val condition: Condition
)

data class HourlyForecast(
    @SerializedName("time_epoch")
    val timeEpoch: Long,
    @SerializedName("temp_c")
    val tempC: Float,
    @SerializedName("temp_f")
    val tempF: Float,
    val condition: Condition,
) {
    fun toDataModel(): by.resliv.weathermonitor.model.HourlyForecast {
        return by.resliv.weathermonitor.model.HourlyForecast(
            timeEpoch = timeEpoch,
            tempC = tempC,
            tempF = tempF,
            text = condition.text,
            icon = condition.icon
        )
    }
}
