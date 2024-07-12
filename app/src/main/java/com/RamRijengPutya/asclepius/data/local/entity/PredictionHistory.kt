package com.RamRijengPutya.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_history")
data class PredictionHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "image_uri") val imageUri: String,
    @ColumnInfo(name = "prediction_result") val predictionResult: String,
    @ColumnInfo(name = "confidence_score") val confidenceScore: Float
)
