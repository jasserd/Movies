package com.example.movies.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movies.presentation.ui.screens.main.BottomBarTab
import com.example.movies.presentation.ui.screens.main.MainScreen

// TODO: REFACTORING

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val currentTab = when {
        currentDestination?.hasRoute<MoviesRoute>() == true -> {
            BottomBarTab.MOVIES
        }

        currentDestination?.hasRoute<FavoritesRoute>() == true -> {
            BottomBarTab.FAVORITES
        }

        else -> null
    }

    MainScreen(
        currentTab = currentTab,
        onTabClick = { selectedTab ->
            when (selectedTab) {
                BottomBarTab.MOVIES -> navController.navigate(MoviesRoute) {
                    popUpTo(
                        navController.graph.findStartDestination().id
                    ) {
                        saveState = true
                    }

                    launchSingleTop = true
                    restoreState = true
                }

                BottomBarTab.FAVORITES -> navController.navigate(FavoritesRoute) {
                    popUpTo(
                        navController.graph.findStartDestination().id
                    ) {
                        saveState = true
                    }

                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    ) {
        AppNavHost(navController)
    }
}