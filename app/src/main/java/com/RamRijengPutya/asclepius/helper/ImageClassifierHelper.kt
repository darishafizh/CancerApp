package com.RamRijengPutya.asclepius.helper

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private val context: Context,
    private val threshold: Float = 0.1f,
    private val maxResults: Int = 3,
    private val modelName: String = "cancer_classification.tflite",
    private val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(maxResults)
            .setScoreThreshold(threshold)
            .build()

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context, modelName, options
            )
        } catch (e: Exception) {
            classifierListener?.onError("Failed to load the model.")
            Log.e("ImageClassifierHelper", "Error loading model: ${e.message}")
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri))
        val tensorImage = TensorImage.fromBitmap(bitmap)

        val results = imageClassifier?.classify(tensorImage)
        val classifications = results?.get(0)?.categories

        val recognizedClasses = classifications?.filter { it.score > threshold }?.map { it.label }
        val formattedConfidenceScores = classifications?.filter { it.score > threshold }?.map {
            Log.d("ConfidenceScore", "Score before format: ${it.score}")
            String.format("%.2f%%", it.score * 100)
        }

        classifierListener?.onResults(recognizedClasses, formattedConfidenceScores)
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            recognizedClasses: List<String>?,
            confidenceScores: List<String>?
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}

