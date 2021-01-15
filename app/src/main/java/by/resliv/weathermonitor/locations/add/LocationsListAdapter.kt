package by.resliv.weathermonitor.locations.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.resliv.weathermonitor.databinding.AddLocationItemBinding
import by.resliv.weathermonitor.model.Location

/**
 * @author EpicDima
 */
class LocationsListAdapter(
    private val chooseListener: (Location) -> Unit,
) :
    ListAdapter<Location, LocationsListAdapter.LocationViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(
                oldItem: Location,
                newItem: Location
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Location,
                newItem: Location
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AddLocationItemBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding, chooseListener)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class LocationViewHolder(
        private val binding: AddLocationItemBinding,
        private val chooseListener: (Location) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location) {
            binding.location = location
            binding.root.setOnClickListener {
                chooseListener(location)
            }
        }
    }
}