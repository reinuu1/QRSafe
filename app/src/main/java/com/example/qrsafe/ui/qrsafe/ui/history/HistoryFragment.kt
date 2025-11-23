package com.example.qrsafe.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrsafe.data.AppDatabase
import com.example.qrsafe.databinding.FragmentHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter()
        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycler.adapter = adapter

        loadHistory()

        // Buton de ștergere
        binding.clearHistoryBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val dao = AppDatabase.getInstance(requireContext()).linkDao()
                dao.clearAll()
                withContext(Dispatchers.Main) {
                    adapter.submitList(emptyList())
                    binding.emptyText.visibility = View.VISIBLE
                    binding.historyRecycler.visibility = View.GONE
                }
            }
        }
    }

    private fun loadHistory() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getInstance(requireContext()).linkDao()
            val links = dao.getAllLinks()

            withContext(Dispatchers.Main) {
                if (links.isEmpty()) {
                    binding.emptyText.visibility = View.VISIBLE
                    binding.historyRecycler.visibility = View.GONE
                } else {
                    binding.emptyText.visibility = View.GONE
                    binding.historyRecycler.visibility = View.VISIBLE
                    adapter.submitList(links)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
