package com.ajrock.knowyourwoof.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajrock.knowyourwoof.data.IScoresRepository
import com.ajrock.knowyourwoof.data.QuizDataSource
import com.ajrock.knowyourwoof.data.ScoreEntity
import com.ajrock.knowyourwoof.model.QuizItem
import com.ajrock.knowyourwoof.ui.state.QuizUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val MAX_QUIZ_ITEMS = 3
private const val BASE_IMG_URL = "https://upload.wikimedia.org/wikipedia/commons"

class QuizViewModel(private val repository: IScoresRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    private var _allDoggos = setOf<String>()

    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    val baseImageUrl = BASE_IMG_URL
    val maxQuizItemCount = MAX_QUIZ_ITEMS

    init {
        Log.d("ViewModel", "QuizViewModel initialized")
        fetchAllDoggos()
        resetQuiz()
    }

    fun checkAnswer(answer: String)
    {
        val finished = becameFinished()
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

        if (finished) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val now = OffsetDateTime.now(ZoneOffset.UTC).format(formatter)

            viewModelScope.launch {
                repository.insertScore(ScoreEntity(
                    correctAnswers = _uiState.value.answered,
                    totalAttempts = maxQuizItemCount,
                    date = now
                ))
            }
        }
    }

    fun nextQuizItem() {
        if (_uiState.value.finished) {
            // quiz is already finished
            resetQuiz()
            return
        }

        if (becameFinished()) {
            // quiz just became finished without checking answer validity
            _uiState.update { currentState ->
                currentState.copy(
                    finished = true
                )
            }
            return
        }

        // usual flow of answering and going to next quiz item
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

    private fun becameFinished() = _uiState.value.currentQuizIndex >= MAX_QUIZ_ITEMS - 1;

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