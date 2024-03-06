package com.ajrock.knowyourwoof.viewmodel

import androidx.lifecycle.ViewModel
import com.ajrock.knowyourwoof.data.QuizDataSource
import com.ajrock.knowyourwoof.model.QuizItem
import com.ajrock.knowyourwoof.ui.state.QuizUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val MAX_QUIZ_ITEMS = 3
private const val BASE_IMG_URL = "https://upload.wikimedia.org/wikipedia/commons"

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    private var _allDoggos = setOf<String>()

    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    val baseImageUrl = BASE_IMG_URL
    val maxQuizItemCount = MAX_QUIZ_ITEMS

    init {
        fetchAllDoggos()
        resetQuiz()
    }

    fun checkAnswer(answer: String)
    {
        val finished = _uiState.value.currentQuizIndex >= MAX_QUIZ_ITEMS - 1
        val finalAssessmentMsg = if (finished) {
            " Correctly answered %d out of %d"
        }
        else ""

        if (answer == _uiState.value.currentQuizItem.doggo) {
            _uiState.update { currentState ->
                currentState.copy(
                    assessmentMessage = "Correct!${finalAssessmentMsg}"
                        .format(_uiState.value.answered + 1, maxQuizItemCount),
                    answered = currentState.answered + 1,
                    finished = finished
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    assessmentMessage = "No, you donkey! Correct answer is ${currentState.currentQuizItem.doggo}${finalAssessmentMsg}"
                        .format(_uiState.value.answered, maxQuizItemCount),
                    finished = finished
                )
            }
        }
    }

    fun nextQuizItem() {
        if (_uiState.value.finished) {
            resetQuiz()
            return
        }

        val pickedItemPair = pickCurrentQuizOptions()
        _uiState.update { currentState ->
            currentState.copy(
                currentQuizItem = pickedItemPair.first,
                currentQuizIndex = currentState.currentQuizIndex + 1,
                options = pickedItemPair.second,
                assessmentMessage = ""
            )
        }
    }

    private fun resetQuiz() {
        val pickedItemPair = pickCurrentQuizOptions()
        _uiState.value = QuizUiState(
            currentQuizItem = pickedItemPair.first,
            options = pickedItemPair.second
        )
    }

    private fun pickCurrentQuizOptions(): Pair<QuizItem, List<String>>
    {
        val currentQuizItem = QuizDataSource.items.random()
        val doggos = _allDoggos.minus(currentQuizItem.doggo)
        val option1 = doggos.random()
        val option2 = doggos.minus(option1).random()

        return Pair(currentQuizItem, listOf(currentQuizItem.doggo, option1, option2).shuffled())
    }

    private fun fetchAllDoggos()
    {
        _allDoggos = QuizDataSource.items.map {
            it.doggo
        }.toSet()
    }
}