package by.resliv.weathermonitor.locations.list

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.resliv.weathermonitor.R
import by.resliv.weathermonitor.databinding.LocationsListFragmentBinding
import by.resliv.weathermonitor.locations.add.AddLocationActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author EpicDima
 */
@AndroidEntryPoint
class LocationsListFragment : Fragment(), LocationListener {
    companion object {
        const val PERMISSIONS_REQUEST_CODE = 23423

        fun newInstance() = LocationsListFragment()
    }

    private lateinit var binding: LocationsListFragmentBinding
    private val viewModel: LocationsListViewModel by viewModels()
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationsListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLocationsListAdapter()
    }

    private fun setLocationsListAdapter() {
        val locationsListAdapter = LocationsForecastListAdapter(viewModel.measurementUnits, {
            viewModel.chooseLocation(it)
            requireActivity().finish()
        }) {
            viewModel.removeLocation(it)
        }
        binding.locationsRecyclerview.adapter = locationsListAdapter
        binding.locationsRecyclerview.layoutManager = LinearLayoutManager(context)
        viewModel.items.observe(viewLifecycleOwner) { locationsListAdapter.submitList(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.locations_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.current_location -> {
                determineCurrentLocation()
                true
            }
            R.id.add_location -> {
                startActivity(Intent(requireContext(), AddLocationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun determineCurrentLocation() {
        if (permissionDenied()) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSIONS_REQUEST_CODE
            )
            return
        }
        requestCurrentLocation()
    }

    private fun permissionDenied(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestCurrentLocation() {
        val criteria = Criteria()
        criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
        locationManager.requestSingleUpdate(criteria, this, Looper.myLooper())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCurrentLocation()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        viewModel.addCurrentLocation(location) {
            requireActivity().runOnUiThread {
                requireActivity().finish()
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}
}