package com.ajrock.knowyourwoof

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.ajrock.knowyourwoof.extensions.isExpanded
import com.ajrock.knowyourwoof.ui.ROOT_ROUTE_WELCOME
import com.ajrock.knowyourwoof.ui.aboutScreen
import com.ajrock.knowyourwoof.ui.components.WoofNavigationItems
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
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ROOT_ROUTE_WELCOME

    val navSuiteType = if (windowSizeClass.isExpanded)
        NavigationSuiteType.NavigationRail
    else
        NavigationSuiteType.NavigationBar

    NavigationSuiteScaffoldLayout(
        layoutType = navSuiteType,
        navigationSuite = {
            WoofNavigationItems(
                currentRoute = currentRoute,
                isScreenExpanded = windowSizeClass.isExpanded,
                onNavigateToRoute = { route ->
                    navController.navigate(route)
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = ROOT_ROUTE_WELCOME
        ) {
            welcomeScreen { navController.navigateToQuizScreen() }
            quizScreen(quizViewModel, windowSizeClass.isExpanded)
            statsScreen()
            aboutScreen()
        }
    }
}

