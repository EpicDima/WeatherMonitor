package by.resliv.weathermonitor.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @author EpicDima
 */
@Entity(
    tableName = "daily",
    foreignKeys = [ForeignKey(
        entity = Location::class,
        parentColumns = ["id"],
        childColumns = ["locationId"],
        onDelete = ForeignKey.CASCADE,
        deferred = true
    )],
    indices = [Index(value = ["locationId", "dateEpoch"], unique = true)]
)
data class DailyForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val locationId: Int = 0,
    val dateEpoch: Long,
    val maxTempC: Float,
    val maxTempF: Float,
    val minTempC: Float,
    val minTempF: Float,
    val text: String,
    val icon: String
)
