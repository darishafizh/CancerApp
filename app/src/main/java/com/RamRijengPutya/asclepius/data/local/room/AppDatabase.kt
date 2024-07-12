package com.RamRijengPutya.asclepius.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.RamRijengPutya.asclepius.data.local.entity.PredictionHistory

@Database(entities = [PredictionHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun predictionHistoryDao(): PredictionHistoryDao
}