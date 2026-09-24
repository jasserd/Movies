package com.example.movies.presentation.ui.screens.movies.details

sealed interface MovieDetailsIntent {

    data object RetryClicked: MovieDetailsIntent
}