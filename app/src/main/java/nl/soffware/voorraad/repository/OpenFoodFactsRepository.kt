package nl.soffware.voorraad.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.withTimeout
import nl.soffware.voorraad.api.OpenFoodFactsAPI
import nl.soffware.voorraad.model.Product
import nl.soffware.voorraad.service.OpenFoodFactsService

class OpenFoodFactsRepository {
    private val openFoodFactsService: OpenFoodFactsService = OpenFoodFactsAPI.createApi()

    private val _scannedProduct: MutableLiveData<Product> = MutableLiveData()

    val scannedProduct: LiveData<Product>
        get() = _scannedProduct

    suspend fun fetchScannedProduct(barcode: String) {
        try {
            val result = withTimeout(5_000) {
                openFoodFactsService.getProductForBarcode(barcode)
            }
            _scannedProduct.value = result.product
        } catch (error: Throwable) {
            throw ProductFetchError("Unable to fetch product for barcode $barcode", error)
        }
    }

    class ProductFetchError(message: String, cause: Throwable) : Throwable(message, cause)
}