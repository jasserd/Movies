package com.example.movies.presentation.ui.screens.favorites

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.movies.R
import com.example.movies.presentation.ui.components.AppTopBar

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.favorites)
            )
        }
    )
    { innerPadding ->
        FavoritesContent(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun FavoritesContent(modifier: Modifier = Modifier) {
    Text(
        text = "Favorites",
        modifier = modifier
    )
}
