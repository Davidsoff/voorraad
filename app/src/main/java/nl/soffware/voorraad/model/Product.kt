package nl.soffware.voorraad.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(

    var image_url: String = "",
    var product_name: String = "",
    var quantity: String = "",
    @Json(name = "_id")
    var barcode: String = ""
)