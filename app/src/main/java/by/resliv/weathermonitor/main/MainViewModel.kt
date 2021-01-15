package by.resliv.weathermonitor.main

import android.app.Application
import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import by.resliv.weathermonitor.data.Repository
import by.resliv.weathermonitor.model.CurrentForecast
import by.resliv.weathermonitor.model.DailyForecast
import by.resliv.weathermonitor.model.HourlyForecast
import by.resliv.weathermonitor.model.Location
import by.resliv.weathermonitor.schedule.RefreshWorker
import by.resliv.weathermonitor.schedule.checkIfScheduleWorkExisting
import by.resliv.weathermonitor.settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MainViewModel @ViewModelInject constructor(
    application: Application,
    private val repository: Repository,
    private val preferences: SharedPreferences
) : AndroidViewModel(application), SharedPreferences.OnSharedPreferenceChangeListener {

    private val _empty = MutableLiveData(true)
    val empty: LiveData<Boolean> = _empty

    private val _locations = repository.observeAllLocations()

    private val _choosed = MutableLiveData(0)

    private val _location = Transformations.switchMap(_choosed) {
        repository.observeLocation(it)
    }
    val location: LiveData<Location> = _location

    private val _currentForecast = Transformations.switchMap(_location) {
        if (it != null) {
            repository.observeCurrentForecast(it.id)
        } else {
            MutableLiveData()
        }
    }
    val currentForecast: LiveData<CurrentForecast> = _currentForecast

    private val _dailyForecast = Transformations.switchMap(_location) { location ->
        repository.observeDailyForecast(location?.id ?: 0)
            .map { it.sortedBy { forecast -> forecast.dateEpoch } }
    }
    val dailyForecast: LiveData<List<DailyForecast>> = _dailyForecast

    private val _hourlyForecast = Transformations.switchMap(_dailyForecast) { list ->
        repository.observeHourlyForecast(list.firstOrNull()?.id ?: 0)
            .map { it.sortedBy { forecast -> forecast.timeEpoch } }
    }
    val hourlyForecast: LiveData<List<HourlyForecast>> = _hourlyForecast

    private val _measurementUnits = MutableLiveData(preferences.getMeasurementUnits())
    val measurementUnits: LiveData<MeasurementUnits> = _measurementUnits

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
        viewModelScope.launch(Dispatchers.Default) {
            checkIfScheduleWorkExisting(application, preferences.getInt(SCHEDULE_KEY).toLong())
        }
        _locations.observeForever {
            checkChoosedLocation(it)
        }
    }

    private fun checkChoosedLocation(list: List<Location>) {
        val choosedFromPreferences = preferences.getInt(CHOOSED_KEY)
        val choosed =
            if (list.map { it.id }.contains(choosedFromPreferences)) choosedFromPreferences else 0
        _empty.value = (choosed == 0)
        if (choosed != choosedFromPreferences) {
            preferences.set(CHOOSED_KEY, choosed)
        }
        _choosed.value = choosed
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        _measurementUnits.value = preferences.getMeasurementUnits()
        checkChoosedLocation(_locations.value!!)
    }

    override fun onCleared() {
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    fun refresh(callback: Runnable) {
        viewModelScope.launch(Dispatchers.IO) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val refreshTask = OneTimeWorkRequestBuilder<RefreshWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager
                .getInstance(getApplication())
                .enqueue(refreshTask)
                .result
                .addListener(callback, Executors.newSingleThreadExecutor())
        }
    }
}