package by.resliv.weathermonitor.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.resliv.weathermonitor.databinding.DailyForecastItemBinding
import by.resliv.weathermonitor.model.DailyForecast
import by.resliv.weathermonitor.settings.MeasurementUnits

/**
 * @author EpicDima
 */
class DailyForecastAdapter(
    private val units: LiveData<MeasurementUnits>
) :
    ListAdapter<DailyForecast, DailyForecastAdapter.DailyForecastViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DailyForecastItemBinding.inflate(inflater, parent, false)
        return DailyForecastViewHolder(binding, units)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class DailyForecastViewHolder(
        private val binding: DailyForecastItemBinding,
        private val units: LiveData<MeasurementUnits>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: DailyForecast) {
            binding.forecast = forecast
            units.observeForever {
                binding.units = it
            }
        }
    }
}