package com.ajrock.knowyourwoof

import android.app.Application
import com.ajrock.knowyourwoof.data.IScoresRepository
import com.ajrock.knowyourwoof.data.OfflineScoresRepository
import com.ajrock.knowyourwoof.data.ScoreDatabase
import com.ajrock.knowyourwoof.viewmodel.QuizViewModel
import com.ajrock.knowyourwoof.viewmodel.StatsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}

internal val appModule = module {
    single<IScoresRepository> {
        OfflineScoresRepository(ScoreDatabase.getDatabase(androidContext()).scoreDao())
    }

    viewModel<QuizViewModel>()
    viewModel<StatsViewModel>()
}