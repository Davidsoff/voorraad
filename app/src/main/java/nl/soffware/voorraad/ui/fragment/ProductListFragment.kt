package nl.soffware.voorraad.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.*
import nl.soffware.voorraad.databinding.FragmentProductListBinding
import nl.soffware.voorraad.model.Location
import nl.soffware.voorraad.model.Product
import nl.soffware.voorraad.ui.adapter.LocationAdapter
import nl.soffware.voorraad.ui.adapter.ProductRecyclerViewAdapter
import nl.soffware.voorraad.ui.view_model.ActivityBarViewModel
import nl.soffware.voorraad.ui.view_model.SupplyCloset

/**
 * A fragment representing a list of Items.
 */
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SupplyCloset by activityViewModels()
    private val abViewModel: ActivityBarViewModel by activityViewModels()

    private lateinit var locations: MutableList<Location>
    private lateinit var products: MutableList<Product>
    private lateinit var productAdapter: ProductRecyclerViewAdapter

    private fun initVars() {
        locations = viewModel.locations.value ?: emptyList<Location>().toMutableList()
        products = locations[viewModel.selectedLocation].products
        productAdapter = ProductRecyclerViewAdapter(products)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        initVars()
        initViews()
        createItemTouchHelper()
        toggleEmptyMessage()
        observeProducts()
        return binding.root
    }

    private fun initViews() {
        with(binding.list) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = productAdapter
            this.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            createItemTouchHelper().attachToRecyclerView(this)
        }
        abViewModel.updateActionBarTitle(locations[viewModel.selectedLocation].name)
    }

    private fun observeProducts() {
        viewModel.locations.observe(viewLifecycleOwner, {
            products = it[viewModel.selectedLocation].products
            productAdapter.notifyDataSetChanged()
            toggleEmptyMessage()
        })
    }

    private fun toggleEmptyMessage() {
        if (products.isEmpty()) {
            binding.list.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        }
        else {
            binding.list.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                locations[viewModel.selectedLocation].products.removeAt(position)
                viewModel.upsertLocation(locations[viewModel.selectedLocation])
                productAdapter.notifyItemRemoved(position)
            }
        }
        return ItemTouchHelper(callback)
    }
}