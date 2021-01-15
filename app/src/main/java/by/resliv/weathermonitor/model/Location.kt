package by.resliv.weathermonitor.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @author EpicDima
 */
@Entity(tableName = "locations", indices = [Index(value = ["name", "auto"], unique = true)])
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val auto: Boolean = false
)
