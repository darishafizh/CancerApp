package com.RamRijengPutya.asclepius.view.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.RamRijengPutya.asclepius.data.local.room.AppDatabase
import com.RamRijengPutya.asclepius.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "riwayat_database").build()
        adapter = HistoryAdapter()

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter

        loadHistoryData()
    }

    private fun loadHistoryData() {
        database.predictionHistoryDao().getAllPredictions().observe(this, Observer { historyList ->
            adapter.submitList(historyList)
        })
    }
}