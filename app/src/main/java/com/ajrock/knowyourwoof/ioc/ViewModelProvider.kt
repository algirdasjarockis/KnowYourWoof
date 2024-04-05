package com.ajrock.knowyourwoof.ioc

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ajrock.knowyourwoof.App
import com.ajrock.knowyourwoof.viewmodel.QuizViewModel
import com.ajrock.knowyourwoof.viewmodel.StatsViewModel

object ViewModelProvider {
    val factory = viewModelFactory {
        initializer {
            QuizViewModel(App().container.scoresRepository)
        }

        initializer {
            StatsViewModel(App().container.scoresRepository)
        }
    }
}

fun CreationExtras.App(): App =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App)