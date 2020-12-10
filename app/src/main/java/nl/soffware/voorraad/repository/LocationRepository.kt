package nl.soffware.voorraad.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.squareup.moshi.Moshi
import nl.soffware.voorraad.model.Location
import nl.soffware.voorraad.model.Product

class LocationRepository {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val locationsCollection =  db.collection("locations")

    val locations: MutableLiveData<MutableList<Location>> = MutableLiveData(emptyList<Location>().toMutableList())

    init {
        startListener()
    }

    private fun startListener(){
        locationsCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val list = emptyList<Location>().toMutableList()
            for (doc in snapshot!!.documents) {
                val location = doc.toObject(Location::class.java)!!
                Log.i(TAG, location.name)
                list.add(location)
            }

            locations.value = list
        }
    }

    fun upsertLocation(location: Location){
        locationsCollection.document(location.name)
            .set(location)
    }
}