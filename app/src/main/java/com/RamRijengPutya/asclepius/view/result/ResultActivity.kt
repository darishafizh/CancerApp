package com.RamRijengPutya.asclepius.view.result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.RamRijengPutya.asclepius.R
import com.RamRijengPutya.asclepius.data.local.entity.PredictionHistory
import com.RamRijengPutya.asclepius.data.local.room.AppDatabase
import com.RamRijengPutya.asclepius.databinding.ActivityResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "riwayat_database").build()

        val predictionResult = intent.getStringExtra("PREDICTION_RESULT")
        val confidenceScore = intent.getStringExtra("CONFIDENCE_SCORE") ?: "0"
        val imageUriString = intent.getStringExtra("IMAGE_URI")

        if (predictionResult != null && imageUriString != null) {
            binding.resultText.text = getString(R.string.prediction_result_format, predictionResult, confidenceScore)
            binding.resultImage.setImageURI(Uri.parse(imageUriString))

            savePredictionToDatabase(imageUriString, predictionResult, confidenceScore)
        } else {
            showToast("Informasi tidak lengkap untuk disimpan.")
        }

        val imageUri = Uri.parse(imageUriString)
        if (imageUri != null) {
            binding.resultImage.setImageURI(imageUri)
        } else {
            showToast("Gagal memuat gambar.")
        }
    }

    private fun savePredictionToDatabase(imageUri: String, predictionResult: String, confidenceScore: String) {
        val history = PredictionHistory(
            imageUri = imageUri,
            predictionResult = predictionResult,
            confidenceScore = confidenceScore.replace("%", "").toFloatOrNull() ?: 0f
        )

        CoroutineScope(Dispatchers.IO).launch {
            database.predictionHistoryDao().insert(history)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}