package com.ajrock.knowyourwoof.ui.state

import com.ajrock.knowyourwoof.data.ScoreEntity

data class StatsUiState(
    val entries : List<ScoreEntity> = emptyList()
)
