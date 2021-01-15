package by.resliv.weathermonitor.locations.list

import android.app.Application
import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import by.resliv.weathermonitor.data.Repository
import by.resliv.weathermonitor.model.CurrentForecast
import by.resliv.weathermonitor.model.Location
import by.resliv.weathermonitor.settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias LocationForecast = Pair<Location, CurrentForecast>

/**
 * @author EpicDima
 */
class LocationsListViewModel @ViewModelInject constructor(
    application: Application,
    private val repository: Repository,
    private val preferences: SharedPreferences
) : AndroidViewModel(application), SharedPreferences.OnSharedPreferenceChangeListener {

    private val _locations = repository.observeAllLocations().map { it.reversed() }

    private val _forecasts = Transformations.switchMap(_locations) {
        val liveData = MutableLiveData<List<CurrentForecast>>(emptyList())
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<CurrentForecast>()
            for (location in it) {
                list.add(repository.getCurrentForecast(location.id))
            }
            liveData.postValue(list)
        }
        liveData
    }

    private val _items = Transformations.map(_forecasts) { _locations.value!!.zip(it) }
    val items: LiveData<List<LocationForecast>> = _items

    private val _measurementUnits = MutableLiveData(preferences.getMeasurementUnits())
    val measurementUnits: LiveData<MeasurementUnits> = _measurementUnits

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        _measurementUnits.value = preferences.getMeasurementUnits()
    }

    override fun onCleared() {
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    fun removeLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeLocation(location)
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (preferences.getInt(CHOOSED_KEY) == location.id) {
                chooseLocation(_locations.value?.firstOrNull()?.id ?: 0)
            }
        }
    }

    fun chooseLocation(location: Location) {
        chooseLocation(location.id)
    }

    private fun chooseLocation(locationId: Int) {
        preferences.set(CHOOSED_KEY, locationId)
    }

    fun addCurrentLocation(location: android.location.Location, callback: Runnable) {
        val modelLocation = Location(
            name = "",
            latitude = location.latitude,
            longitude = location.longitude,
            auto = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            val id = repository.refreshForecast(modelLocation, false)
            chooseLocation(id)
            callback.run()
        }
    }
}