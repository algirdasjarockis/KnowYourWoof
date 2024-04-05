package com.ajrock.knowyourwoof.data

import kotlinx.coroutines.flow.Flow

class OfflineScoresRepository(private val scoreDao: ScoreDao) : IScoresRepository {
    override suspend fun insertScore(score: ScoreEntity) = scoreDao.insert(score)

    override fun getScoresByDateStream(date: String): Flow<List<ScoreEntity>> = scoreDao.fetchScoresByDate(date)

    override fun getAllScoresStream(): Flow<List<ScoreEntity>> = scoreDao.fetchAllScoresGroupedByDate()
}
