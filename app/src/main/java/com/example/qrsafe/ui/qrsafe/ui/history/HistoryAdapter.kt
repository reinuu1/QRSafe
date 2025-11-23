package com.example.qrsafe.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qrsafe.data.LinkEntity
import com.example.qrsafe.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items: List<LinkEntity> = emptyList()

    fun submitList(newList: List<LinkEntity>) {
        items = newList
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.binding.urlText.text = item.url
        holder.binding.statusText.text = item.status
        holder.binding.timeText.text = java.text.SimpleDateFormat(
            "HH:mm:ss dd/MM/yyyy", java.util.Locale.getDefault()
        ).format(item.timestamp)

        val color = when (item.status) {
            "SAFE" -> 0xFFBBFFBB.toInt()
            "MALICIOUS" -> 0xFFFFCDD2.toInt()
            else -> 0xFFFFF9C4.toInt()
        }
        holder.binding.root.setCardBackgroundColor(color)
    }

    override fun getItemCount(): Int = items.size
}
