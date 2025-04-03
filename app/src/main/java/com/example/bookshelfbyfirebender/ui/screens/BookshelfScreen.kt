package com.example.bookshelfbyfirebender.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelfbyfirebender.R

@Composable
fun BookshelfHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: BookshelfViewModel = viewModel()
) {
    when (viewModel.bookshelfUiState) {
        is BookshelfUiState.Loading -> LoadingScreen(modifier)
        is BookshelfUiState.Success -> SuccessScreen(
            books = (viewModel.bookshelfUiState as BookshelfUiState.Success).books,
            modifier = modifier
        )
        is BookshelfUiState.Error -> ErrorScreen(modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.loading_failed))
    }
}

@Composable
fun SuccessScreen(
    books: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = books)
    }
}