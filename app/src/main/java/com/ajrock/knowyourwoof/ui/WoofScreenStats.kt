package com.ajrock.knowyourwoof.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ajrock.knowyourwoof.data.ScoreEntity
import com.ajrock.knowyourwoof.ui.components.WoofAppBar
import com.ajrock.knowyourwoof.ui.state.StatsUiState

@Composable
fun WoofScreenStats(
    uiState: StatsUiState,
    modifier: Modifier = Modifier,
    isScreenExpanded: Boolean = false,
    onNavigateUp: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            if (!isScreenExpanded) {
                WoofAppBar(title = "Stats", onNavigateUp = onNavigateUp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            if (uiState.entries.isEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(text = "Stats")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp),
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxHeight()
                ) {
                    items(
                        uiState.entries,
                        { item -> item.id }
                    ) { score ->
                        StatsItemCard(score)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsItemCard(score: ScoreEntity)
{
    val scoreRatio = score.correctAnswers / score.totalAttempts.toFloat()
    val scorePercent = Math.round(scoreRatio * 100)

    Card(
        modifier = Modifier.padding(bottom = 12.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = score.date,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(text = "Correct answers: ${score.correctAnswers}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Total attempts: ${score.totalAttempts}")
                }

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$scorePercent%"
                    )

                    CircularProgressIndicator(
                        progress = { scoreRatio },
                        strokeWidth = 5.dp,
                        trackColor = Color.Red,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun WoofScreeStatsPreview() {
    var idx = 0
    WoofScreenStats(StatsUiState(listOf(
        ScoreEntity(++idx, 10, 20, "2024-01-01"),
        ScoreEntity(++idx, 10, 20, "2024-01-02"),
        ScoreEntity(++idx, 10, 30, "2024-01-02"),
        ScoreEntity(++idx, 10, 20, "2024-01-02"),
    )))
}