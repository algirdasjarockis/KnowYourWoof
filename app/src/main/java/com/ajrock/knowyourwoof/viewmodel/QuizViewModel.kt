package com.ajrock.knowyourwoof.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajrock.knowyourwoof.data.IScoresRepository
import com.ajrock.knowyourwoof.data.ScoreEntity
import com.ajrock.knowyourwoof.data.repository.IDogRepository
import com.ajrock.knowyourwoof.model.BreedModel
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
// skip these images
// https://images.dog.ceo/breeds/shihtzu/n02086240_7195.jpg
// https://images.dog.ceo/breeds/husky/n02110185_6473.jpg
// https://images.dog.ceo/breeds/germanshepherd/n02106662_22394.jpg

class QuizViewModel(
    private val repository: IScoresRepository,
    private val doggosRepo: IDogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    private var _allDoggos = setOf<BreedModel>()

    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        Log.d("ViewModel", "QuizViewModel initialized")

        viewModelScope.launch {
            _allDoggos = doggosRepo.fetchAllBreeds().toSet()
            resetQuiz()
        }
    }

    fun checkAnswer(answer: String)
    {
        val finished = becameFinished()
        val finalAssessmentMsg = if (finished) {
            " Correctly answered %d out of %d"
        }
        else ""

        if (answer == _uiState.value.currentQuizItem.doggo.displayName) {
            _uiState.update { currentState ->
                currentState.copy(
                    assessmentMessage = "Correct!${finalAssessmentMsg}"
                        .format(_uiState.value.answered + 1, MAX_QUIZ_ITEMS),
                    answered = currentState.answered + 1,
                    finished = finished
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    assessmentMessage = "No, you donkey! Correct answer is ${currentState.currentQuizItem.doggo}${finalAssessmentMsg}"
                        .format(_uiState.value.answered, MAX_QUIZ_ITEMS),
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
                    totalAttempts = MAX_QUIZ_ITEMS,
                    date = now
                ))
            }
        }
    }

    fun nextQuizItem() {
        viewModelScope.launch {
            prepareNextQuizItem()
        }
    }

    private fun becameFinished() = _uiState.value.currentQuizIndex >= MAX_QUIZ_ITEMS - 1;

    private suspend fun resetQuiz() {
        val pickedItemPair = pickCurrentQuizOptions()
        _uiState.value = QuizUiState(
            currentQuizItem = pickedItemPair.first,
            totalQuizItems = MAX_QUIZ_ITEMS,
            options = pickedItemPair.second
        )
    }

    private suspend fun prepareNextQuizItem() {
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

    private suspend fun pickCurrentQuizOptions(): Pair<QuizItem, List<String>>
    {
        val randomBreed = _allDoggos.random()
        val imageUrl = doggosRepo.fetchImageByBreed(randomBreed)

        if (imageUrl.isNullOrBlank()) {
            throw Exception("Couldn't get image path")
        }

        val currentQuizItem = QuizItem(randomBreed, imageUrl)
        val doggos = _allDoggos.minus(randomBreed)
        val option1 = doggos.random()
        val option2 = doggos.minus(option1).random()

        return Pair(
            currentQuizItem,
            listOf(currentQuizItem.doggo.displayName, option1.displayName, option2.displayName
        ).shuffled())
    }
}