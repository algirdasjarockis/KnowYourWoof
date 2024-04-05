package com.ajrock.knowyourwoof.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajrock.knowyourwoof.data.IScoresRepository
import com.ajrock.knowyourwoof.ui.state.StatsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel(private val repository : IScoresRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(StatsUiState())

    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllScoresStream().collect { newEntries ->
                _uiState.update { currentState ->
                    currentState.copy(entries = newEntries)
                }
            }
        }
    }
}