package by.resliv.weathermonitor.locations.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.resliv.weathermonitor.databinding.LocationsListItemBinding
import by.resliv.weathermonitor.model.Location
import by.resliv.weathermonitor.settings.MeasurementUnits

/**
 * @author EpicDima
 */
class LocationsForecastListAdapter(
    private val units: LiveData<MeasurementUnits>,
    private val chooseListener: (Location) -> Unit,
    private val deleteListener: (Location) -> Unit,
) :
    ListAdapter<LocationForecast, LocationsForecastListAdapter.LocationForecastViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocationForecast>() {
            override fun areItemsTheSame(
                oldItem: LocationForecast,
                newItem: LocationForecast
            ): Boolean {
                return oldItem.first.id == newItem.first.id
            }

            override fun areContentsTheSame(
                oldItem: LocationForecast,
                newItem: LocationForecast
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LocationsListItemBinding.inflate(inflater, parent, false)
        return LocationForecastViewHolder(binding, units, chooseListener, deleteListener)
    }

    override fun onBindViewHolder(holder: LocationForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class LocationForecastViewHolder(
        private val binding: LocationsListItemBinding,
        private val units: LiveData<MeasurementUnits>,
        private val chooseListener: (Location) -> Unit,
        private val deleteListener: (Location) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(locationForecast: LocationForecast) {
            binding.location = locationForecast.first
            binding.forecast = locationForecast.second
            units.observeForever {
                binding.units = it
            }
            binding.root.setOnClickListener {
                chooseListener(locationForecast.first)
            }
            binding.delete.setOnClickListener {
                deleteListener(locationForecast.first)
            }
        }
    }
}