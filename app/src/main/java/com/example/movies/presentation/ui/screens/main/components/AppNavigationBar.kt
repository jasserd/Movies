package com.example.movies.presentation.ui.screens.main.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movies.R
import com.example.movies.presentation.ui.screens.main.BottomBarTab

@Composable
fun AppNavigationBar(
    currentTab: BottomBarTab,
    onTabClick: (BottomBarTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        // TODO: REFACTORING
        NavigationBarItem(
            selected = currentTab == BottomBarTab.MOVIES,
            onClick = { onTabClick(BottomBarTab.MOVIES) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_movie),
                    contentDescription = stringResource(R.string.movies),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(R.string.movies)) },
        )
        NavigationBarItem(
            selected = currentTab == BottomBarTab.FAVORITES,
            onClick = { onTabClick(BottomBarTab.FAVORITES) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = stringResource(R.string.favorites),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(R.string.favorites)) },
        )
    }
}