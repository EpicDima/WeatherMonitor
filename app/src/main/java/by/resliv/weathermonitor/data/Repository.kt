package by.resliv.weathermonitor.data

import androidx.lifecycle.LiveData
import by.resliv.weathermonitor.data.network.RetrofitApiService
import by.resliv.weathermonitor.data.storage.dao.CurrentForecastDao
import by.resliv.weathermonitor.data.storage.dao.DailyForecastDao
import by.resliv.weathermonitor.data.storage.dao.HourlyForecastDao
import by.resliv.weathermonitor.data.storage.dao.LocationDao
import by.resliv.weathermonitor.model.CurrentForecast
import by.resliv.weathermonitor.model.DailyForecast
import by.resliv.weathermonitor.model.HourlyForecast
import by.resliv.weathermonitor.model.Location
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author EpicDima
 */
@Singleton
class Repository @Inject constructor(
    private val apiService: RetrofitApiService,
    private val locationDao: LocationDao,
    private val currentForecastDao: CurrentForecastDao,
    private val dailyForecastDao: DailyForecastDao,
    private val hourlyForecastDao: HourlyForecastDao
) {
    suspend fun getLocationsForQuery(query: String): List<Location> {
        return apiService.getLocations(query).map { it.toDataModel() }
    }

    fun observeLocation(id: Int): LiveData<Location> {
        return locationDao.select(id)
    }

    fun observeAllLocations(): LiveData<List<Location>> {
        return locationDao.selectLiveData()
    }

    suspend fun getAllLocations(): List<Location> {
        return locationDao.select()
    }

    private suspend fun saveLocation(location: Location): Int {
        val locations = getAllLocations()
        if (location.auto) {
            val temp = locations.find { it.auto }
            return if (temp == null) {
                locationDao.insert(location).toInt()
            } else {
                locationDao.update(location.copy(id = temp.id))
                temp.id
            }
        } else if (location.id != 0) {
            if (locations.find { it.id == location.id } != null) {
                locationDao.update(location)
                return location.id
            } else {
                return locationDao.insert(location).toInt()
            }
        } else {
            val temp = locations.find { it.name == location.name && it.auto == location.auto }
            return if (temp == null) {
                locationDao.insert(location).toInt()
            } else {
                locationDao.update(location.copy(id = temp.id))
                temp.id
            }
        }
    }

    suspend fun removeLocation(location: Location) {
        locationDao.delete(location)
    }

    fun observeCurrentForecast(locationId: Int): LiveData<CurrentForecast> {
        return currentForecastDao.selectLiveData(locationId)
    }

    suspend fun getCurrentForecast(locationId: Int): CurrentForecast {
        return currentForecastDao.select(locationId)
    }

    fun observeDailyForecast(locationId: Int): LiveData<List<DailyForecast>> {
        return dailyForecastDao.select(locationId)
    }

    fun observeHourlyForecast(dailyId: Int): LiveData<List<HourlyForecast>> {
        return hourlyForecastDao.select(dailyId)
    }

    suspend fun refreshForecast(location: Location, name: Boolean = true): Int {
        val response =
            apiService.getForecast(if (name) location.name else "${location.latitude},${location.longitude}")
        val locationId =
            saveLocation(response.toLocationModel().copy(id = location.id, auto = location.auto))
        val currentForecast = response.toCurrentForecastModel().copy(locationId = locationId)
        currentForecastDao.upsert(currentForecast)
        val dailyAndHourlyForecast = response.toDailyAndHourlyForecastModel()
        dailyAndHourlyForecast.first.zip(dailyAndHourlyForecast.second).forEach {
            val dailyId = dailyForecastDao.upsert(it.first.copy(locationId = locationId)).toInt()
            it.second.forEach { hourly ->
                hourlyForecastDao.upsert(hourly.copy(dailyId = dailyId))
            }
        }
        return locationId
    }
}