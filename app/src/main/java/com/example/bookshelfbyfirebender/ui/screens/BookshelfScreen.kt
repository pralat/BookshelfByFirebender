package com.example.bookshelfbyfirebender.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelfbyfirebender.R
import com.example.bookshelfbyfirebender.network.Book
import com.example.bookshelfbyfirebender.ui.screens.BookshelfUiState
import com.example.bookshelfbyfirebender.ui.screens.BookshelfViewModel

@Composable
fun BookshelfHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: BookshelfViewModel = BookshelfViewModel()
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
    books: List<Book>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = books) { book ->
            BookCard(book = book)
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(296.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:"))
                .crossfade(true)
                .build(),
            contentDescription = book.volumeInfo.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = book.volumeInfo.title,
            modifier = Modifier.padding(8.dp)
        )
    }
}