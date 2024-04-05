package com.ajrock.knowyourwoof.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * from scores WHERE date = :date")
    fun fetchScoresByDate(date: String): Flow<List<ScoreEntity>>

    @Query("SELECT * from scores ORDER BY date DESC")
    fun fetchAllScores(): Flow<List<ScoreEntity>>

    @Query("SELECT id," +
            " DATE(date) AS date," +
            " sum(correctAnswers) AS correctAnswers," +
            " sum(totalAttempts) AS totalAttempts" +
            " FROM scores" +
            " GROUP BY DATE(date)" +
            " ORDER BY date DESC")
    fun fetchAllScoresGroupedByDate(): Flow<List<ScoreEntity>>
}