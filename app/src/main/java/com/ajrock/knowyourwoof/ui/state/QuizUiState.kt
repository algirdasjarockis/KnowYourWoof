package com.ajrock.knowyourwoof.ui.state

import com.ajrock.knowyourwoof.model.DogPhotoItem
import com.ajrock.knowyourwoof.model.QuizItem

data class QuizUiState(
    val currentQuizItem: QuizItem = QuizItem("", DogPhotoItem("", "")),
    val options: List<String> = emptyList(),
    val answered: Int = 0,
    val assessmentMessage: String = ""
)
