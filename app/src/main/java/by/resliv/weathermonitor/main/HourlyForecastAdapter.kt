package by.resliv.weathermonitor.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.resliv.weathermonitor.databinding.HourlyForecastItemBinding
import by.resliv.weathermonitor.model.HourlyForecast
import by.resliv.weathermonitor.settings.MeasurementUnits

/**
 * @author EpicDima
 */
class HourlyForecastAdapter(
    private val units: LiveData<MeasurementUnits>
) :
    ListAdapter<HourlyForecast, HourlyForecastAdapter.HourlyForecastViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HourlyForecast>() {
            override fun areItemsTheSame(
                oldItem: HourlyForecast,
                newItem: HourlyForecast
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: HourlyForecast,
                newItem: HourlyForecast
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HourlyForecastItemBinding.inflate(inflater, parent, false)
        return HourlyForecastViewHolder(binding, units)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class HourlyForecastViewHolder(
        private val binding: HourlyForecastItemBinding,
        private val units: LiveData<MeasurementUnits>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: HourlyForecast) {
            binding.forecast = forecast
            units.observeForever {
                binding.units = it
            }
        }
    }
}