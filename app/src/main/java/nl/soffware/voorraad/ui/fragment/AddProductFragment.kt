package nl.soffware.voorraad.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import nl.soffware.voorraad.R
import nl.soffware.voorraad.databinding.FragmentAddProductBinding
import nl.soffware.voorraad.model.Location
import nl.soffware.voorraad.model.Product
import nl.soffware.voorraad.ui.view_model.ScannedBarcode
import nl.soffware.voorraad.ui.view_model.SupplyCloset


class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScannedBarcode by activityViewModels()
    private val supplyCloset: SupplyCloset by activityViewModels()
    private lateinit var scannedProduct: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.search).isVisible = false
        menu.findItem(R.id.save_product).isVisible = true
        menu.findItem(R.id.save_product).setOnMenuItemClickListener {
            save()
            navigateBack()
            true
        }

    }

    private fun navigateBack() {
        findNavController().navigate(viewModel.origin, null, NavOptions.Builder()
            .setPopUpTo(viewModel.origin,true).build())
    }

    private fun save() {
        val locations = supplyCloset.locations.value ?: emptyList<Location>().toMutableList()
        val selectedLocationId = locations.indexOf(binding.spinnerLocation.selectedItem as Location)
        supplyCloset.selectedLocation = selectedLocationId
        val name = binding.etProductName.text.toString()
        val type = binding.etProductType.text.toString()
        val newProduct = Product(scannedProduct.image_url, name, type, scannedProduct.barcode)
        locations[selectedLocationId].products.add(newProduct)
        supplyCloset.upsertLocation(locations[selectedLocationId])
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        binding.progressbar.visibility = View.VISIBLE
        observeScannedProduct()
        setupSpinner()

        return binding.root
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, supplyCloset.locationsList)
        if(supplyCloset.selectedLocation >= 0) {
            binding.spinnerLocation.setSelection(supplyCloset.selectedLocation)
        }
        binding.spinnerLocation.adapter = adapter

    }

    private fun observeScannedProduct() {
        viewModel.scannedProduct.observe(viewLifecycleOwner, ::productChanged)
    }

    private fun productChanged(product: Product) {
        scannedProduct = product
        if (scannedProduct.barcode == viewModel.lastBarcode) {
            updateFields(scannedProduct)
            binding.progressbar.visibility = View.GONE
        } else {
            binding.progressbar.visibility = View.VISIBLE
        }
    }

    private fun updateFields(product: Product) {
        binding.tvBarcode.text = product.barcode
        binding.etProductName.setText(product.product_name)
        binding.etProductType.setText(product.quantity)
        binding.ivImage.layout(0, 0, 0, 0)
        Glide.with(requireContext()).load(product.image_url).into(binding.ivImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}