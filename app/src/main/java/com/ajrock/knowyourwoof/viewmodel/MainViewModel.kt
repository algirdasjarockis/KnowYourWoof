package com.ajrock.knowyourwoof.viewmodel

import androidx.lifecycle.ViewModel
import com.ajrock.knowyourwoof.data.QuizDataSource
import com.ajrock.knowyourwoof.model.QuizItem
import com.ajrock.knowyourwoof.ui.state.QuizUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    private var _allDoggos = setOf<String>()

    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    val baseImageUrl = "https://upload.wikimedia.org/wikipedia/commons"

    init {
        fetchAllDoggos()
        resetQuiz()
    }

    fun checkAnswer(answer: String)
    {
        if (answer == _uiState.value.currentQuizItem.doggo) {
            _uiState.update { currentState ->
                currentState.copy(
                    assessmentMessage = "Correct!",
                    answered = currentState.answered + 1
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    assessmentMessage = "No, you donkey! Correct answer is ${currentState.currentQuizItem.doggo}",
                )
            }
        }
    }

    fun nextQuizItem() {
        val pickedItemPair = pickCurrentQuizOptions()
        _uiState.update { currentState ->
            currentState.copy(
                currentQuizItem = pickedItemPair.first,
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

//    fun rollDice() {
//        _uiState.update { currentState ->
//            currentState.copy(
//                firstDieValue = Random.nextInt(from = 1, until = 7),
//                secondDieValue = Random.nextInt(from = 1, until = 7),
//                numberOfRolls = currentState.numberOfRolls + 1,
//            )
//        }
//    }

}