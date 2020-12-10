package nl.soffware.voorraad.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result (
    @field:Json(name = "product")
    val product: Product
)