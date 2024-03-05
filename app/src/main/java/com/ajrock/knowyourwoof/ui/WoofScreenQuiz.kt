package com.ajrock.knowyourwoof.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.onConsumedWindowInsetsChanged
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ajrock.knowyourwoof.R
import com.ajrock.knowyourwoof.data.QuizDataSource
import com.ajrock.knowyourwoof.model.QuizItem

@Composable
fun WoofScreenQuiz(
    quizItem: QuizItem,
    options: List<String>,
    modifier: Modifier = Modifier,
    message: String = "",
    baseImageUrl: String = "",
    onAnswerSelected: (String) -> Unit = {},
    onNextButtonClick: () -> Unit = {},
) {
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
                    .data(baseImageUrl + quizItem.photo.url)
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

        Box {
            SelectionGroup(
                options = options,
                onSelected = onAnswerSelected,
                modifier = Modifier.alpha(if (message.isEmpty()) 1f else 0f)
            )
            NextQuizItem(
                message = message,
                onNextButtonClick = onNextButtonClick,
                modifier = Modifier.alpha(if (message.isEmpty()) 0f else 1f)
            )
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

@Preview(showSystemUi = true)
@Composable
fun WoofScreeQuizPreview() {
    val item = QuizDataSource.items.first()
    WoofScreenQuiz(
        quizItem = item,
        message = "",
        options = listOf(item.doggo, "Dog2", "Dog3").shuffled()
    )
}