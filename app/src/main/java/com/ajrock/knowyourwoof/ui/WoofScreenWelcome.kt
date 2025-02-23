package com.ajrock.knowyourwoof.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ajrock.knowyourwoof.R

@Composable
fun WoofScreenWelcome(
    onStartQuizClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        // Designed by Freepik
        Image(
            painter = painterResource(id = R.drawable.doggo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .height(300.dp)
                .width(300.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(20.dp))
        Button(onClick = onStartQuizClicked) {
            Text(text = "Are you ready?", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Woof!", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun WoofScreeWelcomePreview(

) {
    WoofScreenWelcome()
}