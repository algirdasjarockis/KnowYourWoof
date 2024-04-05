package com.ajrock.knowyourwoof.ioc

import android.content.Context
import com.ajrock.knowyourwoof.data.OfflineScoresRepository
import com.ajrock.knowyourwoof.data.ScoreDatabase

/**
 * App container for Dependency injection.
 */
interface IAppContainer {
    val scoresRepository: OfflineScoresRepository
}

class AppContainer(private val context: Context) : IAppContainer {
    override val scoresRepository: OfflineScoresRepository by lazy {
        OfflineScoresRepository(ScoreDatabase.getDatabase(context).scoreDao())
    }
}