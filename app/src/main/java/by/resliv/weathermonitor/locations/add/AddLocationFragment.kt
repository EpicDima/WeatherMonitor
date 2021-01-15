package by.resliv.weathermonitor.locations.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.resliv.weathermonitor.databinding.AddLocationFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class AddLocationFragment : Fragment() {
    companion object {
        fun newInstance() = AddLocationFragment()
    }

    private lateinit var binding: AddLocationFragmentBinding
    private val viewModel: AddLocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddLocationFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLocationsListAdapter()
        binding.locationSearch.requestFocus()
        binding.locationSearch.addTextChangedListener {
            viewModel.search(it.toString())
        }
    }

    private fun setLocationsListAdapter() {
        val locationsListAdapter = LocationsListAdapter {
            viewModel.saveLocation(it)
            requireActivity().finish()
        }
        binding.locationsRecyclerview.adapter = locationsListAdapter
        binding.locationsRecyclerview.layoutManager = LinearLayoutManager(context)
        viewModel.locations.observe(viewLifecycleOwner) { locationsListAdapter.submitList(it) }
    }
}