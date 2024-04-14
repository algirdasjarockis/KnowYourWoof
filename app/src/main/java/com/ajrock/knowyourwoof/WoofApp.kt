package com.ajrock.knowyourwoof

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ajrock.knowyourwoof.ui.ROOT_ROUTE_ABOUT
import com.ajrock.knowyourwoof.ui.ROOT_ROUTE_QUIZ
import com.ajrock.knowyourwoof.ui.ROOT_ROUTE_STATS
import com.ajrock.knowyourwoof.ui.ROOT_ROUTE_WELCOME
import com.ajrock.knowyourwoof.ui.aboutScreen
import com.ajrock.knowyourwoof.ui.isNavIconVisible
import com.ajrock.knowyourwoof.ui.navigateToQuizScreen
import com.ajrock.knowyourwoof.ui.quizScreen
import com.ajrock.knowyourwoof.ui.statsScreen
import com.ajrock.knowyourwoof.ui.welcomeScreen
import com.ajrock.knowyourwoof.viewmodel.QuizViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WoofApp(
    navController: NavHostController = rememberNavController(),
    quizViewModel: QuizViewModel = koinViewModel(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ROOT_ROUTE_WELCOME

    Scaffold(
        topBar = {
            var title = "Woof!"
//            if (currentRoute == WoofRoute.Quiz)
//                title = "%d/%d question".format(uiState.currentQuizIndex + 1, viewModel.maxQuizItemCount)
            WoofAppBar(navController = navController, title = title)
        },
        bottomBar = {
            WoofNavigationBar(currentRoute, navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROOT_ROUTE_WELCOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            welcomeScreen { navController.navigateToQuizScreen() }
            quizScreen(quizViewModel)
            statsScreen()
            aboutScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WoofAppBar(
    navController: NavHostController,
    title: String,
    modifier: Modifier = Modifier
) {
    val showNavIcon = navController.isNavIconVisible()

    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (showNavIcon) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        }
    )
}

private enum class WoofNavigationItem(
    val route: String,
    val labelText: String,
    @DrawableRes val icon: Int
) {
    Quiz(route = ROOT_ROUTE_QUIZ, labelText = "Quiz", icon = R.drawable.doggo_24px),
    Stats(route = ROOT_ROUTE_STATS, labelText = "Statistics", icon = R.drawable.bar_chart_4_bars_24px),
    About(route = ROOT_ROUTE_ABOUT, labelText = "About", icon = R.drawable.pets_24px)
}

@Composable
private fun WoofNavigationBar(
    currentRoute: String,
    navController: NavHostController
) {
    val items = listOf(WoofNavigationItem.Quiz, WoofNavigationItem.Stats, WoofNavigationItem.About)

    NavigationBar {
        items.forEach { item ->
            val onSameRoute = currentRoute == item.route

            NavigationBarItem(
                selected = onSameRoute,
                onClick = {
                    if (!onSameRoute)
                        navController.navigate(item.route)
                },
                label = { Text(text = item.labelText) },
                icon = { Icon(painterResource(item.icon), null) }
            )
        }
    }
}