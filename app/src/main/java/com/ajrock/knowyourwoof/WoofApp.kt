package com.ajrock.knowyourwoof

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ajrock.knowyourwoof.viewmodel.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ajrock.knowyourwoof.ui.WoofScreenQuiz
import com.ajrock.knowyourwoof.ui.WoofScreenWelcome

enum class WoofRoute {
    Welcome,
    Quiz,
    Stats,
    About
}

enum class WoofNavigationItem(val route: WoofRoute, val labelText: String) {
    Quiz(route = WoofRoute.Quiz, labelText = "Quiz"),
    Stats(route = WoofRoute.Stats, labelText = "Statistics"),
    About(route = WoofRoute.About, labelText = "About")
}

@Composable
fun WoofApp(
    viewModel: MainViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = WoofRoute.valueOf(
        backStackEntry?.destination?.route ?: WoofRoute.Welcome.name
    )

    Scaffold(
        bottomBar = {
            WoofNavigationBar(currentRoute, navController)
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = WoofRoute.Welcome.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = WoofRoute.Welcome.name) {
                WoofScreenWelcome(onStartQuizClicked = { navController.navigate(WoofRoute.Quiz.name) })
            }
            composable(route = WoofRoute.Quiz.name) {
                WoofScreenQuiz(
                    quizItem = uiState.currentQuizItem,
                    baseImageUrl = viewModel.baseImageUrl,
                    options = uiState.options,
                    message = uiState.assessmentMessage,
                    onAnswerSelected = { viewModel.checkAnswer(it) },
                    onNextButtonClick = { viewModel.nextQuizItem() }
                )
            }
            composable(route = WoofRoute.Stats.name) {

            }
            composable(route = WoofRoute.About.name) {

            }
        }
    }
}

@Composable
fun WoofNavigationBar(
    currentRoute: WoofRoute,
    navController: NavHostController
) {
    val items = listOf(WoofNavigationItem.Quiz, WoofNavigationItem.Stats, WoofNavigationItem.About)

    NavigationBar(

    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route.name) },
                label = { Text(text = item.labelText)},
                icon = { Icon(Icons.Filled.Favorite, null) }
            )
        }
    }
}