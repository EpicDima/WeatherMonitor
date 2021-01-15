package by.resliv.weathermonitor.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.*
import by.resliv.weathermonitor.R
import by.resliv.weathermonitor.schedule.updateScheduleWork
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(context).apply {
            addSchedulePreference(this)
            addUnitsPreferences(this)
        }
    }

    private fun addSchedulePreference(screen: PreferenceScreen) {
        screen.addPreference(ListPreference(context).apply {
            key = SCHEDULE_KEY
            title = getString(R.string.update_every)
            entries = arrayOf(
                getString(R.string.minutes_15),
                getString(R.string.minutes_30),
                getString(R.string.hour_1),
                getString(R.string.hours_2),
                getString(R.string.hours_4),
                getString(R.string.hours_8),
                getString(R.string.hours_12)
            )
            entryValues = getStringValues(SCHEDULE_KEY)
            setDefaultValue(this@SettingsFragment.sharedPreferences.getString(SCHEDULE_KEY))
            setOnPreferenceChangeListener { _, newValue ->
                updateScheduleWork(requireContext(), newValue.toString().toLong())
                this@SettingsFragment.sharedPreferences.set(SCHEDULE_KEY, newValue)
                true
            }
        })
    }

    private fun addUnitsPreferences(screen: PreferenceScreen) {
        screen.apply {
            title = getString(R.string.units)
            addPreference(
                createStringListPreference(
                    TEMPERATURE_KEY,
                    R.string.temperature,
                    R.string.temperature_c_full,
                    R.string.temperature_f_full
                ).apply {
                    setOnPreferenceChangeListener { _, newValue ->
                        this@SettingsFragment.sharedPreferences.set(TEMPERATURE_KEY, newValue)
                        true
                    }
                }
            )
            addPreference(
                createStringListPreference(
                    WIND_KEY,
                    R.string.wind,
                    R.string.wind_kph_full,
                    R.string.wind_mph_full
                ).apply {
                    setOnPreferenceChangeListener { _, newValue ->
                        this@SettingsFragment.sharedPreferences.set(WIND_KEY, newValue)
                        true
                    }
                }
            )
            addPreference(
                createStringListPreference(
                    PRECIP_KEY,
                    R.string.precip,
                    R.string.precip_mm_full,
                    R.string.precip_in_full
                ).apply {
                    setOnPreferenceChangeListener { _, newValue ->
                        this@SettingsFragment.sharedPreferences.set(PRECIP_KEY, newValue)
                        true
                    }
                }
            )
            addPreference(
                createStringListPreference(
                    PRESSURE_KEY,
                    R.string.pressure,
                    R.string.pressure_mm_full,
                    R.string.pressure_in_full
                ).apply {
                    setOnPreferenceChangeListener { _, newValue ->
                        this@SettingsFragment.sharedPreferences.set(PRESSURE_KEY, newValue)
                        true
                    }
                }
            )
            addPreference(
                createStringListPreference(
                    VISIBILITY_KEY,
                    R.string.visibility,
                    R.string.visibility_km_full,
                    R.string.visibility_mi_full
                ).apply {
                    setOnPreferenceChangeListener { _, newValue ->
                        this@SettingsFragment.sharedPreferences.set(VISIBILITY_KEY, newValue)
                        true
                    }
                }
            )
        }
    }

    private fun createStringListPreference(
        key: String,
        titleId: Int,
        vararg entries: Int
    ): ListPreference {
        return ListPreference(context).apply {
            this.key = key
            title = getString(titleId)
            this.entries = entries.map { getString(it) }.toTypedArray()
            entryValues = getStringValues(key)
            setDefaultValue(this@SettingsFragment.sharedPreferences.getString(key))
        }
    }

}