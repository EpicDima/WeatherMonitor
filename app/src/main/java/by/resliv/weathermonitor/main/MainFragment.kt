package by.resliv.weathermonitor.main

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.resliv.weathermonitor.R
import by.resliv.weathermonitor.databinding.MainFragmentBinding
import by.resliv.weathermonitor.locations.list.LocationsListActivity
import by.resliv.weathermonitor.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setHourlyForecastAdapter()
        setDailyForecastAdapter()
        setListeners()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.empty = viewModel.empty
        binding.location = viewModel.location
        binding.forecast = viewModel.currentForecast
        binding.units = viewModel.measurementUnits
    }

    private fun setHourlyForecastAdapter() {
        val hourlyForecastAdapter = HourlyForecastAdapter(viewModel.measurementUnits)
        binding.hourlyForecastRecyclerview.adapter = hourlyForecastAdapter
        binding.hourlyForecastRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyForecastAdapter.submitList(it) }
    }

    private fun setDailyForecastAdapter() {
        val dailyForecastAdapter = DailyForecastAdapter(viewModel.measurementUnits)
        binding.dailyForecastRecyclerview.adapter = dailyForecastAdapter
        binding.dailyForecastRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.dailyForecast.observe(viewLifecycleOwner) { dailyForecastAdapter.submitList(it) }
    }

    private fun setListeners() {
        binding.chooseButton.setOnClickListener {
            startActivity(Intent(requireContext(), LocationsListActivity::class.java))
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh {
                binding.refreshLayout.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
                true
            }
            R.id.locations -> {
                startActivity(Intent(requireContext(), LocationsListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}