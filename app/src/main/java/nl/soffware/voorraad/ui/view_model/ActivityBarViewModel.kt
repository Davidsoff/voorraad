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

class ActivityBarViewModel(application: Application): AndroidViewModel(application){
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title
    fun updateActionBarTitle(title: String) = _title.postValue(title)
}