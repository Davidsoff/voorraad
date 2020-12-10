package nl.soffware.voorraad.ui.fragment

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import nl.soffware.voorraad.R
import nl.soffware.voorraad.databinding.FragmentBarcodeScannerBinding
import nl.soffware.voorraad.ui.view_model.ScannedBarcode
import java.io.IOException


class BarcodeScannerFragment : Fragment() {
    private var _binding: FragmentBarcodeScannerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScannedBarcode by activityViewModels()

    var cameraSource: CameraSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeScannerBinding.inflate(inflater, container, false)


        val barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.EAN_8 or Barcode.EAN_13)
            .build()

        binding.cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(CAMERA), 9001
                        )
                        return
                    }
                    cameraSource = CameraSource.Builder(context, barcodeDetector)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedFps(15.0f)
                        .setAutoFocusEnabled(true)
                        .build()
                    cameraSource?.start(binding.cameraView.holder)
                } catch (ie: IOException) {
                    Log.e("CAMERA SOURCE", ie.message ?: "")
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    viewModel.lastBarcode = barcodes.valueAt(0).rawValue
                    viewModel.fetchScannedProduct(viewModel.lastBarcode)
                    this@BarcodeScannerFragment.findNavController()
                        .navigate(R.id.action_barcodeScannerFragment_to_addProductFragment)
                }
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}