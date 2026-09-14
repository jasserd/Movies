package com.example.movies.presentation.ui.screens.main
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.movies.presentation.ui.screens.main.components.AppNavigationBar

enum class BottomBarTab {
    MOVIES,
    FAVORITES
}

// TODO: REFACTORING

@Composable
fun MainScreen(
    currentTab: BottomBarTab?,
    onTabClick: (BottomBarTab) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            currentTab?.let {
                AppNavigationBar(
                    currentTab = it,
                    onTabClick = onTabClick
                )
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                content()
            }
        }
    )
}