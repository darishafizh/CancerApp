package com.RamRijengPutya.asclepius.view.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.RamRijengPutya.asclepius.data.local.entity.PredictionHistory
import com.RamRijengPutya.asclepius.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<PredictionHistory, HistoryAdapter.HistoryViewHolder>(DiffCallback()) {

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: PredictionHistory) {
            binding.tvPrediction.text = history.predictionResult
            binding.tvScore.text = String.format("%.2f%%", history.confidenceScore * 100)

            if (history.imageUri.isNotEmpty()) {
                val imageUri = Uri.parse(history.imageUri)
                binding.imageView.setImageURI(imageUri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = getItem(position)
        holder.bind(historyItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<PredictionHistory>() {
        override fun areItemsTheSame(oldItem: PredictionHistory, newItem: PredictionHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PredictionHistory, newItem: PredictionHistory): Boolean {
            return oldItem == newItem
        }
    }
}