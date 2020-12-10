package nl.soffware.voorraad.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.soffware.voorraad.R
import nl.soffware.voorraad.databinding.ActivityMainBinding
import nl.soffware.voorraad.ui.view_model.ActivityBarViewModel
import nl.soffware.voorraad.ui.view_model.ScannedBarcode
import nl.soffware.voorraad.ui.view_model.SupplyCloset


class MainActivity : AppCompatActivity() {
    // Can be set to anything, most people prefer a value of 9000
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private val viewModel: ScannedBarcode by viewModels()
    private val abViewModel: ActivityBarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        FirebaseFirestore.setLoggingEnabled(true)
        FirebaseApp.initializeApp(this)

        abViewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY) ?: ""
        }
    }

    override fun onStart() {
        super.onStart()
        navController = findNavController(R.id.nav_host_fragment)
        binding.fab.setOnClickListener {
            viewModel.origin = navController.currentDestination?.id ?: 0
            navController.navigate(R.id.barcodeScannerFragment)
        }
        fabToggler()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.save_product).isVisible = false
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        checkGooglePlayServices()
    }

    private fun checkGooglePlayServices() {
        // check if the correct version of google play services is installed and available.
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
            }
        }
    }

    private fun fabToggler() {
        navController.addOnDestinationChangedListener { _,       destination, _ ->
            if (destination.id in arrayOf(
                    R.id.barcodeScannerFragment,
                    R.id.addProductFragment
                )) {
                binding.fab.hide()
            } else {
                binding.fab.show()
            }
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

}