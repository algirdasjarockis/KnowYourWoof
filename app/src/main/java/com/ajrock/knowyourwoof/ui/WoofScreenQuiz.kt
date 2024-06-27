package com.ajrock.knowyourwoof.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ajrock.knowyourwoof.R
import com.ajrock.knowyourwoof.model.BreedModel
import com.ajrock.knowyourwoof.model.QuizItem
import com.ajrock.knowyourwoof.ui.state.QuizUiState

@Composable
fun WoofScreenQuiz(
    uiState: QuizUiState,
    modifier: Modifier = Modifier,
    onNextButtonClick: () -> Unit = {},
    onAnswerSelect: (String) -> Unit = {},
) {
    val message = uiState.assessmentMessage

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
    ) {
        Text(text = "%d of %d".format(uiState.currentQuizIndex + 1, uiState.totalQuizItems))
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                //.padding(horizontal = 12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiState.currentQuizItem.photo)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.doggo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
            )
        }

        Spacer(modifier = Modifier
            .height(32.dp)
            .weight(1f))

        Column(
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            if (uiState.finished) {
                FinishedQuizItem(
                    message = message,
                    onNextButtonClick = onNextButtonClick,
                    modifier = modifier
                )
            } else {
                if (message.isEmpty()) {
                    SelectionGroup(
                        options = uiState.options,
                        onSelected = onAnswerSelect,
                        modifier = Modifier.alpha(if (message.isEmpty()) 1f else 0f)
                    )
                } else {
                    NextQuizItem(
                        message = message,
                        onNextButtonClick = onNextButtonClick,
                        modifier = Modifier.alpha(if (message.isEmpty()) 0f else 1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectionGroup(
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit = {},
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        options.forEach { option ->
            Button(
                modifier = Modifier.padding(bottom = 4.dp),
                onClick = { onSelected(option) }
            ) {
                Text(
                    text = option,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NextQuizItem(
    message: String,
    onNextButtonClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = message)
        Button(onClick = onNextButtonClick) {
            Text(text = "Next doggo!")
        }
    }
}

@Composable
private fun FinishedQuizItem(
    message: String,
    onNextButtonClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = "Quiz is finished!")
        Text(text = message)
        Button(
            onClick = onNextButtonClick,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(
                text = "Restart!",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun WoofScreeQuizPreview() {
    WoofScreenQuiz(
        QuizUiState(
            assessmentMessage = "",
            finished = true,
            options = listOf("Choice A", "Choice B with very long name", "Choice C"),
            currentQuizItem = QuizItem(
                BreedModel("Some doggo", "Good doggo", null),
                ""
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun FinishedQuizItemPreview() {
    FinishedQuizItem(
        message = "This is some message",
        onNextButtonClick = { },
        modifier = Modifier
    )
}