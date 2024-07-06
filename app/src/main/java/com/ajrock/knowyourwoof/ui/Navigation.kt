package com.ajrock.knowyourwoof.ui

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ajrock.knowyourwoof.viewmodel.QuizViewModel
import com.ajrock.knowyourwoof.viewmodel.StatsViewModel
import org.koin.androidx.compose.koinViewModel

const val ROOT_ROUTE_WELCOME = "welcome"
const val ROOT_ROUTE_QUIZ = "quiz"
const val ROOT_ROUTE_STATS = "stats"
const val ROOT_ROUTE_ABOUT = "about"

fun NavGraphBuilder.welcomeScreen(
    onNavigateToQuiz: () -> Unit
) {
    composable(route = ROOT_ROUTE_WELCOME) {
        WoofScreenWelcome(onStartQuizClicked = onNavigateToQuiz)
    }
}

fun NavGraphBuilder.quizScreen(
    viewModel: QuizViewModel,
    showTwoColumns: Boolean = false
) {
    composable(route = ROOT_ROUTE_QUIZ) {
        val state = viewModel.uiState.collectAsState()
        WoofScreenQuiz(
            uiState = state.value,
            showTwoColumns = showTwoColumns,
            onNextButtonClick = viewModel::nextQuizItem,
            onAnswerSelect = viewModel::checkAnswer
        )
    }
}

fun NavGraphBuilder.statsScreen() {
    composable(route = ROOT_ROUTE_STATS) {
        val statsViewModel = koinViewModel<StatsViewModel>()
        val uiState = statsViewModel.uiState.collectAsState()
        WoofScreenStats(uiState.value)
    }
}

fun NavGraphBuilder.aboutScreen() {
    composable(route = ROOT_ROUTE_ABOUT) {
    }
}

fun NavController.navigateToQuizScreen() {
    navigate(ROOT_ROUTE_QUIZ)
}

fun NavController.isNavIconVisible(): Boolean {
    val currentRoute = getCurrentRoute()
    return getCurrentRoute() != ROOT_ROUTE_QUIZ && currentRoute != ROOT_ROUTE_WELCOME
}

fun NavController.isCurrentRouteScreenQuiz(): Boolean {
    return getCurrentRoute() == ROOT_ROUTE_QUIZ
}

private fun NavController.getCurrentRoute() = currentBackStackEntry?.destination?.route;