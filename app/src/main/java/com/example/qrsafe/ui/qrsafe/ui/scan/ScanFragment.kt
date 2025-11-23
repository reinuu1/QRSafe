package com.example.qrsafe.ui.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.qrsafe.data.AppDatabase
import com.example.qrsafe.data.LinkEntity
import com.example.qrsafe.databinding.FragmentScanBinding
import com.example.qrsafe.network.VirusTotalService
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    // Launcher pentru scanare QR
    private val qrScannerLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            val scannedUrl = result.contents
            binding.urlInput.setText(scannedUrl)
            checkUrlSafety(scannedUrl)
        } else {
            binding.resultCard.visibility = View.VISIBLE
            binding.statusText.text = "❌ Scanare anulată."
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Verificare manuală de link
        binding.scanButton.setOnClickListener {
            val url = binding.urlInput.text.toString().trim()
            if (url.isNotEmpty()) {
                checkUrlSafety(url)
            } else {
                binding.resultCard.visibility = View.VISIBLE
                binding.statusText.text = "❗ Introdu un link valid!"
            }
        }

        // Deschidere camera pentru cod QR
        binding.scanQrButton.setOnClickListener {
            val options = ScanOptions()
            options.setPrompt("Scanează un cod QR pentru verificare")
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.captureActivity = CustomCaptureActivity::class.java
            qrScannerLauncher.launch(options)
        }
    }

    // Verificare VirusTotal
    private fun checkUrlSafety(url: String) {
        val encoded = VirusTotalService.encodeUrlForVirusTotal(url)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = VirusTotalService.api.getUrlReport(encoded)
                val dao = AppDatabase.getInstance(requireContext()).linkDao()

                withContext(Dispatchers.Main) {
                    binding.resultCard.visibility = View.VISIBLE
                    binding.urlText.text = url

                    if (response.isSuccessful) {
                        val stats = response.body()?.data?.attributes?.stats
                        if (stats != null) {
                            val malicious = stats.malicious
                            val harmless = stats.harmless
                            val status = if (malicious > harmless) "MALICIOUS" else "SAFE"

                            binding.resultCard.setCardBackgroundColor(
                                if (status == "MALICIOUS") 0xFFFFCDD2.toInt() else 0xFFBBFFBB.toInt()
                            )
                            binding.statusText.text = if (status == "MALICIOUS")
                                "⚠️ Link periculos detectat!"
                            else
                                "✅ Link sigur"

                            lifecycleScope.launch(Dispatchers.IO) {
                                dao.insert(LinkEntity(url = url, status = status))
                            }
                        } else {
                            binding.statusText.text = "Nu există date pentru acest link."
                        }
                    } else {
                        binding.resultCard.setCardBackgroundColor(0xFFFFF9C4.toInt())
                        binding.statusText.text = "Eroare: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.resultCard.visibility = View.VISIBLE
                    binding.resultCard.setCardBackgroundColor(0xFFFFCDD2.toInt())
                    binding.statusText.text = "Eroare: ${e.message}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
