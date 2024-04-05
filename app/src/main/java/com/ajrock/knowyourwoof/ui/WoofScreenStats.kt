package com.ajrock.knowyourwoof.ui

import android.widget.GridLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ajrock.knowyourwoof.data.ScoreEntity
import com.ajrock.knowyourwoof.ioc.ViewModelProvider
import com.ajrock.knowyourwoof.ui.state.StatsUiState
import com.ajrock.knowyourwoof.viewmodel.StatsViewModel

@Composable
fun WoofScreenStats(
    uiState: StatsUiState,
    modifier: Modifier = Modifier,
    //viewModel: StatsViewModel = viewModel(factory = ViewModelProvider.factory),
) {
    //val uiState by viewModel.uiState.collectAsState()

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
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .height(220.dp)
        ) {
            items(
                uiState.entries,
                { item -> item.id }
            ) { score ->
                Column (
                    modifier = Modifier.padding(bottom = 10.dp).height(40.dp),
                ) {
                    Text(text = score.date, style = TextStyle(fontWeight = FontWeight.Bold))
                    Row(
                    ) {
                        Text(text = "Correct answers: ${score.correctAnswers}")
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Total attempts: ${score.totalAttempts}")
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun WoofScreeStatsPreview() {

    WoofScreenStats(StatsUiState(listOf(
        ScoreEntity(1, 10, 20, "2024-01-01"),
        ScoreEntity(2, 10, 20, "2024-01-02"),
    )))
}