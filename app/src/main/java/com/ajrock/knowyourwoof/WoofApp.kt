package com.ajrock.knowyourwoof

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ajrock.knowyourwoof.ioc.ViewModelProvider
import com.ajrock.knowyourwoof.ui.WoofScreenQuiz
import com.ajrock.knowyourwoof.ui.WoofScreenStats
import com.ajrock.knowyourwoof.ui.WoofScreenWelcome
import com.ajrock.knowyourwoof.viewmodel.QuizViewModel
import com.ajrock.knowyourwoof.viewmodel.StatsViewModel

enum class WoofRoute {
    Welcome,
    Quiz,
    Assessment,
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
    navController: NavHostController = rememberNavController(),
    quizViewModel: QuizViewModel = viewModel(factory = ViewModelProvider.factory),
    //statsViewModel: StatsViewModel = viewModel(factory = ViewModelProvider.factory)
) {
    //val uiState by viewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = WoofRoute.valueOf(
        backStackEntry?.destination?.route ?: WoofRoute.Welcome.name
    )

    Scaffold(
        topBar = {
            var title = "Woof!"
//            if (currentRoute == WoofRoute.Quiz)
//                title = "%d/%d question".format(uiState.currentQuizIndex + 1, viewModel.maxQuizItemCount)
            WoofAppBar(currentRoute = currentRoute, navController = navController, title = title)
        },
        bottomBar = {
            WoofNavigationBar(currentRoute, navController)
        }
    ) { innerPadding ->
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
                WoofScreenQuiz(viewModel = quizViewModel)
            }
            composable(route = WoofRoute.Stats.name) {
                val statsViewModel = viewModel<StatsViewModel>(factory = ViewModelProvider.factory)
                val uiState = statsViewModel.uiState.collectAsState()
                WoofScreenStats(uiState.value)
            }
            composable(route = WoofRoute.About.name) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofAppBar(
    currentRoute: WoofRoute,
    navController: NavHostController,
    title: String,
    modifier: Modifier = Modifier
) {
    val showNavIcon = currentRoute != WoofRoute.Quiz && currentRoute != WoofRoute.Welcome

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

@Composable
fun WoofNavigationBar(
    currentRoute: WoofRoute,
    navController: NavHostController
) {
    val items = listOf(WoofNavigationItem.Quiz, WoofNavigationItem.Stats, WoofNavigationItem.About)

    NavigationBar(

    ) {
        items.forEach { item ->
            val onSameRoute = currentRoute == item.route

            NavigationBarItem(
                selected = onSameRoute,
                onClick = {
                    if (!onSameRoute)
                        navController.navigate(item.route.name)
                },
                label = { Text(text = item.labelText)},
                icon = { Icon(Icons.Filled.Favorite, null) }
            )
        }
    }
}