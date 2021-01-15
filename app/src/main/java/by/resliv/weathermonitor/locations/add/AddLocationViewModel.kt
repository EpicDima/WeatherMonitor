package by.resliv.weathermonitor.locations.add

import android.app.Application
import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import by.resliv.weathermonitor.data.Repository
import by.resliv.weathermonitor.model.Location
import by.resliv.weathermonitor.settings.CHOOSED_KEY
import by.resliv.weathermonitor.settings.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author EpicDima
 */
class AddLocationViewModel @ViewModelInject constructor(
    application: Application,
    private val repository: Repository,
    private val preferences: SharedPreferences
) : AndroidViewModel(application) {

    private val _locations = MutableLiveData<List<Location>>(emptyList())
    val locations: LiveData<List<Location>> = _locations

    fun search(query: String) {
        if (query.length >= 3) {
            viewModelScope.launch(Dispatchers.IO) {
                _locations.postValue(repository.getLocationsForQuery(query))
            }
        } else {
            _locations.value = emptyList()
        }
    }

    fun saveLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = repository.refreshForecast(location, false)
            preferences.set(CHOOSED_KEY, id)
        }
    }
}