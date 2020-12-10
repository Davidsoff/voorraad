package nl.soffware.voorraad.ui.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.soffware.voorraad.model.Product
import nl.soffware.voorraad.repository.OpenFoodFactsRepository

class ScannedBarcode(application: Application): AndroidViewModel(application){
    private val openFoodFactsRepository = OpenFoodFactsRepository()
    lateinit var lastBarcode: String
    val scannedProduct = openFoodFactsRepository.scannedProduct
    var origin = 0

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    /**
     * Expose non MutableLiveData via getter
     * errorText can be observed from view for error showing
     * Encapsulation :)
     */
    val errorText: LiveData<String>
        get() = _errorText

    /**
     * The viewModelScope is bound to Dispatchers.Main and will automatically be cancelled when the ViewModel is cleared.
     * Extension method of lifecycle-viewmodel-ktx library
     */
    fun fetchScannedProduct(barcode: String) {
        viewModelScope.launch {
            try {
                openFoodFactsRepository.fetchScannedProduct(barcode)
            } catch (error: OpenFoodFactsRepository.ProductFetchError) {
                _errorText.value = error.message
                Log.e("Product error", error.cause.toString())
            }
        }
    }
}