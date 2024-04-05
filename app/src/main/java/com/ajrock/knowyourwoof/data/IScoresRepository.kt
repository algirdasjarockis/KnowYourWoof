package com.ajrock.knowyourwoof.data

import kotlinx.coroutines.flow.Flow

interface IScoresRepository {
    suspend fun insertScore(score: ScoreEntity)

    fun getScoresByDateStream(date: String): Flow<List<ScoreEntity>>

    fun getAllScoresStream(): Flow<List<ScoreEntity>>
}