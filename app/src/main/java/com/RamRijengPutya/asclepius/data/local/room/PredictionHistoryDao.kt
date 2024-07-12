package com.RamRijengPutya.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.RamRijengPutya.asclepius.data.local.entity.PredictionHistory

@Dao
interface PredictionHistoryDao {
    @Insert
    fun insert(history: PredictionHistory)

    @Query("SELECT * FROM prediction_history")
    fun getAllPredictions(): LiveData<List<PredictionHistory>>
}