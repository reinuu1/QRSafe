package com.example.qrsafe.ui.scan


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

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
                binding.statusText.text = "Introdu un link valid!"
            }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
