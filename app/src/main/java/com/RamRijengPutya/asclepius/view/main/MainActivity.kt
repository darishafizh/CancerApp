package com.RamRijengPutya.asclepius.view.main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.RamRijengPutya.asclepius.R
import com.RamRijengPutya.asclepius.databinding.ActivityMainBinding
import com.RamRijengPutya.asclepius.helper.ImageClassifierHelper
import com.RamRijengPutya.asclepius.view.history.HistoryActivity
import com.RamRijengPutya.asclepius.view.news.NewsActivity
import com.RamRijengPutya.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }

        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#008000")))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_information -> {
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun startGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    private fun showImage(imageUri: Uri) {
        binding.previewImageView.setImageURI(imageUri)
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            val imageClassifierHelper = ImageClassifierHelper(this, classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(recognizedClasses: List<String>?, confidenceScores: List<String>?) {
                    if (recognizedClasses != null && confidenceScores != null) {
                        val confidenceScore = confidenceScores.first().replace("%", "").toFloat()
                        val confidencePercentage = String.format("%.2f%%", confidenceScore)
                        Log.d("ConfidencePercentage", "Formatted score: $confidencePercentage")
                        moveToResult(recognizedClasses.first(), confidencePercentage)
                    }
                }
            })
            imageClassifierHelper.classifyStaticImage(uri)
        } ?: showToast("Pilih gambar terlebih dahulu")
    }

    private fun moveToResult(predictionResult: String, confidencePercentage: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("PREDICTION_RESULT", predictionResult)
            putExtra("CONFIDENCE_SCORE", confidencePercentage)
            putExtra("IMAGE_URI", currentImageUri.toString())
        }
        startActivity(intent)
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            currentImageUri = data.data
            currentImageUri?.let { uri ->
                startCropActivity(uri)
            }
        } else if (requestCode == CROP_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                currentImageUri = it
                showImage(it)
            }
        }
    }

    private fun startCropActivity(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .start(this, CROP_IMAGE_REQUEST)
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val CROP_IMAGE_REQUEST = UCrop.REQUEST_CROP
    }
}