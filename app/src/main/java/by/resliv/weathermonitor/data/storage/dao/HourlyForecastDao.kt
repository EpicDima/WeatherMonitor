package by.resliv.weathermonitor.data.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import by.resliv.weathermonitor.model.HourlyForecast

/**
 * @author EpicDima
 */
@Dao
interface HourlyForecastDao {

    @Query("SELECT * FROM hourly WHERE dailyId = :dailyId")
    fun select(dailyId: Int): LiveData<List<HourlyForecast>>

    @Insert(onConflict = REPLACE)
    suspend fun upsert(hourlyForecast: HourlyForecast)
}