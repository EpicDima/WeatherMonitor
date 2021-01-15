package by.resliv.weathermonitor.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @author EpicDima
 */
@Entity(
    tableName = "hourly",
    foreignKeys = [ForeignKey(
        entity = DailyForecast::class,
        parentColumns = ["id"],
        childColumns = ["dailyId"],
        onDelete = ForeignKey.CASCADE,
        deferred = true
    )],
    indices = [Index(value = ["dailyId", "timeEpoch"], unique = true)]
)
data class HourlyForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dailyId: Int = 0,
    val timeEpoch: Long,
    val tempC: Float,
    val tempF: Float,
    val text: String,
    val icon: String
)
