package com.ajrock.knowyourwoof.ui.state

import com.ajrock.knowyourwoof.model.BreedModel
import com.ajrock.knowyourwoof.model.QuizItem

data class QuizUiState(
    val currentQuizItem: QuizItem = QuizItem(BreedModel("", "", null), ""),
    val options: List<String> = emptyList(),
    val answered: Int = 0,
    val currentQuizIndex: Int = 0,
    val totalQuizItems: Int = 0,
    val finished: Boolean = false,
    val errorMessage: String = "",
    val assessmentMessage: String = ""
)
