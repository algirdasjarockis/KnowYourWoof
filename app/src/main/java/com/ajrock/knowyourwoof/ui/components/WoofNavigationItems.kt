package com.ajrock.knowyourwoof.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ajrock.knowyourwoof.R
import com.ajrock.knowyourwoof.ui.ROOT_ROUTE_ABOUT
import com.ajrock.knowyourwoof.ui.ROOT_ROUTE_QUIZ
import com.ajrock.knowyourwoof.ui.ROOT_ROUTE_STATS

private enum class WoofNavigationItem(
    val route: String,
    val labelText: String,
    @DrawableRes val icon: Int
) {
    Quiz(route = ROOT_ROUTE_QUIZ, labelText = "Quiz", icon = R.drawable.doggo_24px),
    Stats(
        route = ROOT_ROUTE_STATS,
        labelText = "Statistics",
        icon = R.drawable.bar_chart_4_bars_24px
    ),
    About(route = ROOT_ROUTE_ABOUT, labelText = "About", icon = R.drawable.pets_24px)
}

private val rootNavItems =
    listOf(WoofNavigationItem.Quiz, WoofNavigationItem.Stats, WoofNavigationItem.About)

@Composable
fun WoofNavigationItems(
    currentRoute: String,
    isScreenExpanded: Boolean,
    onNavigateToRoute: (String) -> Unit = {}
) {
    if (isScreenExpanded) {
        NavigationRail(
            containerColor = NavigationSuiteDefaults.colors().navigationBarContainerColor
        ) {
            Spacer(modifier = Modifier.weight(1f))
            rootNavItems.forEach { item ->
                val onSameRoute = currentRoute == item.route
                NavigationRailItem(
                    selected = onSameRoute,
                    onClick = {
                        if (!onSameRoute) {
                            onNavigateToRoute(item.route)
                        }
                    },
                    label = { Text(text = item.labelText) },
                    icon = { Icon(painterResource(item.icon), null) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    } else {
        NavigationSuite {
            rootNavItems.forEach { item ->
                val onSameRoute = currentRoute == item.route
                item(
                    selected = onSameRoute,
                    onClick = {
                        if (!onSameRoute) {
                            onNavigateToRoute(item.route)
                        }
                    },
                    label = { Text(text = item.labelText) },
                    icon = { Icon(painterResource(item.icon), null) }
                )
            }
        }
    }
}

@Composable
@Preview
fun WoofNavigationItemsPreview() {
    WoofNavigationItems(currentRoute = WoofNavigationItem.Quiz.route, isScreenExpanded = false)
}

@Composable
@Preview
fun WoofNavigationItemsOnExpandedPreview() {
    WoofNavigationItems(currentRoute = WoofNavigationItem.Stats.route, isScreenExpanded = true)
}