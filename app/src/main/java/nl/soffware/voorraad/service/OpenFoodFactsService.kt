package nl.soffware.voorraad.service

import nl.soffware.voorraad.model.Result
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsService {

    @GET("product/{barcode}.json")
    suspend fun getProductForBarcode(@Path("barcode") barcode: String) : Result

}
