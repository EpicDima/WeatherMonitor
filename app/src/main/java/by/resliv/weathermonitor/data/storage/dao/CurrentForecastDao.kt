package by.resliv.weathermonitor.data.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import by.resliv.weathermonitor.model.CurrentForecast

/**
 * @author EpicDima
 */
@Dao
interface CurrentForecastDao {

    @Query("SELECT * FROM current WHERE locationId = :locationId")
    suspend fun select(locationId: Int): CurrentForecast

    @Query("SELECT * FROM current WHERE locationId = :locationId")
    fun selectLiveData(locationId: Int): LiveData<CurrentForecast>

    @Insert(onConflict = REPLACE)
    suspend fun upsert(currentForecast: CurrentForecast)
}