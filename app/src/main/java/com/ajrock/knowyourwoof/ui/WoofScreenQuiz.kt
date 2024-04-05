package com.ajrock.knowyourwoof.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ajrock.knowyourwoof.R
import com.ajrock.knowyourwoof.data.QuizDataSource
import com.ajrock.knowyourwoof.ioc.ViewModelProvider
import com.ajrock.knowyourwoof.viewmodel.QuizViewModel

@Composable
fun WoofScreenQuiz(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel(factory = ViewModelProvider.factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val message = uiState.assessmentMessage
    val onNextButtonClick = { viewModel.nextQuizItem() }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Woof! Who am I?")
        Card(
            modifier = Modifier
                //.padding(horizontal = 12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(viewModel.baseImageUrl + uiState.currentQuizItem.photo.url)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.doggo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(300.dp)
                    .aspectRatio(1f)
            )
        }

        Text(text = "Author: %s, Source: wikipedia")
        Spacer(modifier = Modifier.height(32.dp))

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
                    onSelected = { viewModel.checkAnswer(it) },
                    modifier = Modifier.alpha(if (message.isEmpty()) 1f else 0f)
                )
            }
            else {
                NextQuizItem(
                    message = message,
                    onNextButtonClick = onNextButtonClick,
                    modifier = Modifier.alpha(if (message.isEmpty()) 0f else 1f)
                )
            }
        }
    }
}

@Composable
fun SelectionGroup(
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit = {},
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        options.forEach { option ->
            Button(onClick = { onSelected(option) }) {
                Text(text = option)
            }
        }
    }
}

@Composable
fun NextQuizItem(
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
fun FinishedQuizItem(
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
        Button(onClick = onNextButtonClick) {
            Text(text = "Restart!")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun WoofScreeQuizPreview() {
    val item = QuizDataSource.items.first()
    WoofScreenQuiz(
//        quizItem = item,
//        isFinished = true,
//        message = "akdsjfaksdjf",
//        options = listOf(item.doggo, "Dog2", "Dog3").shuffled()
    )
}