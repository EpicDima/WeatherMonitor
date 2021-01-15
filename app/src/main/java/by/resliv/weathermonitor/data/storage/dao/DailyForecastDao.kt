package by.resliv.weathermonitor.data.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import by.resliv.weathermonitor.model.DailyForecast

/**
 * @author EpicDima
 */
@Dao
interface DailyForecastDao {

    @Query("SELECT * FROM daily WHERE locationId = :locationId ORDER BY id DESC LIMIT :days")
    fun select(locationId: Int, days: Int = 5): LiveData<List<DailyForecast>>

    @Insert(onConflict = REPLACE)
    suspend fun upsert(dailyForecast: DailyForecast): Long
}