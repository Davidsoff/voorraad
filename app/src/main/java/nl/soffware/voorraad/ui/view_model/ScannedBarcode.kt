package nl.soffware.voorraad.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ScannedBarcode(application: Application): AndroidViewModel(application){
    lateinit var lastBarcode: String
}