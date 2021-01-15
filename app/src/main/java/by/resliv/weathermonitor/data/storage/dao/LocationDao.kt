package by.resliv.weathermonitor.data.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import by.resliv.weathermonitor.model.Location

/**
 * @author EpicDima
 */
@Dao
interface LocationDao {
    @Query("SELECT * FROM locations WHERE id = :id")
    fun select(id: Int): LiveData<Location>

    @Query("SELECT * FROM locations")
    suspend fun select(): List<Location>

    @Query("SELECT * FROM locations")
    fun selectLiveData(): LiveData<List<Location>>

    @Insert(onConflict = IGNORE)
    suspend fun insert(location: Location): Long

    @Update(onConflict = IGNORE)
    suspend fun update(location: Location)

    @Delete
    suspend fun delete(location: Location)
}