package by.resliv.weathermonitor.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * @author EpicDima
 */
@Entity(
    tableName = "current",
    foreignKeys = [ForeignKey(
        entity = Location::class,
        parentColumns = ["id"],
        childColumns = ["locationId"],
        onDelete = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class CurrentForecast(
    @PrimaryKey
    val locationId: Int = 0,
    val tempC: Float,
    val tempF: Float,
    val text: String,
    val icon: String,
    val windMph: Float,
    val windKph: Float,
    val windDir: String,
    val pressureMb: Float,
    val pressureIn: Float,
    val precipMm: Float,
    val precipIn: Float,
    val humidity: Float,
    val cloud: Float,
    val feelslikeC: Float,
    val feelslikeF: Float,
    val visKm: Float,
    val visMiles: Float,
    val uv: Float,
)
