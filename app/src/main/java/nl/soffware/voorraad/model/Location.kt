package nl.soffware.voorraad.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location (
    var name: String = "",
    var description: String = "",
    var products: MutableList<Product> = emptyList<Product>().toMutableList()
){
    fun replaceProduct(oldProduct: Product, newProduct: Product){
        products.remove(oldProduct)
        products.add(newProduct)
    }

    override fun toString(): String {
        return name
    }


}