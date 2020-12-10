package nl.soffware.voorraad.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.soffware.voorraad.R
import nl.soffware.voorraad.databinding.FragmentHomeBinding
import nl.soffware.voorraad.model.Location
import nl.soffware.voorraad.ui.adapter.LocationAdapter
import nl.soffware.voorraad.ui.view_model.ActivityBarViewModel
import nl.soffware.voorraad.ui.view_model.SupplyCloset

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SupplyCloset by activityViewModels()
    private val abViewModel: ActivityBarViewModel by activityViewModels()
    private val locations = arrayListOf<Location>()
    private val locationAdapter = LocationAdapter(locations, ::onLocationClick)

    private fun onLocationClick(location: Location) {
        val locations = viewModel.locations.value ?: emptyList<Location>().toMutableList()
        viewModel.selectedLocation = locations.indexOf(location)
        findNavController().navigate(R.id.productListFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        observeLocations()
        initViews()
        return binding.root
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvLocations.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvLocations.adapter = locationAdapter
        binding.rvLocations.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        abViewModel.updateActionBarTitle(getString(R.string.app_name))
    }

    private fun observeLocations() {
        viewModel.locations.observe(viewLifecycleOwner, {
            locations.clear()
            locations.addAll(it)
            locationAdapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}