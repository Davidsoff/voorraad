package nl.soffware.voorraad.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.soffware.voorraad.model.Location
import nl.soffware.voorraad.model.Product
import nl.soffware.voorraad.repository.LocationRepository

class SupplyCloset(application: Application): AndroidViewModel(application){
    private val locationRepository = LocationRepository()
    val locations = locationRepository.locations
    var selectedLocation: Int = -1


    val locationsList: Array<Location>
        get() = locations.value?.toTypedArray() ?: emptyArray()


    fun upsertLocation(location: Location) {
        locationRepository.upsertLocation(location)
    }
}