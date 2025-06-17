package com.example.bookshelfbyfirebender.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelfbyfirebender.R
import com.example.bookshelfbyfirebender.network.Book
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelfbyfirebender.ui.screens.BookshelfViewModel
import com.example.bookshelfbyfirebender.ui.screens.BookshelfUiState

@Composable
fun BookshelfHomeScreen(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onQueryChange = { searchQuery = it },
            onImeSearch = {
                if (searchQuery.isNotBlank()) {
                    onSearch(searchQuery)
                    keyboardController?.hide()
                }
            }
        )
        EmptySearchScreen()
    }
}

@Composable
fun BookshelfSearchResultsScreen(
    searchQuery: String,
    onBookClick: (Book) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookshelfViewModel = viewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var currentSearchQuery by remember { mutableStateOf(searchQuery) }

    // Initialize search when screen loads
    LaunchedEffect(searchQuery) {
        currentSearchQuery = searchQuery
        viewModel.updateSearchQuery(searchQuery)
        viewModel.searchBooks()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Search bar with back button
        SearchBarWithBack(
            searchQuery = currentSearchQuery,
            onQueryChange = { newQuery ->
                currentSearchQuery = newQuery
                viewModel.updateSearchQuery(newQuery)
            },
            onSearch = {
                viewModel.searchBooks()
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            onBackClick = onBackClick
        )

        when (viewModel.bookshelfUiState) {
            is BookshelfUiState.Loading -> LoadingScreen()
            is BookshelfUiState.Success -> SuccessScreen(
                books = (viewModel.bookshelfUiState as BookshelfUiState.Success).books,
                onBookClick = onBookClick
            )
            is BookshelfUiState.Error -> ErrorScreen()
            is BookshelfUiState.EmptySearch -> EmptySearchScreen()
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
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
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = books) { book ->
            BookCard(
                book = book,
                onBookClick = onBookClick
            )
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(320.dp)
            .clickable { onBookClick(book) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(book.volumeInfo.imageLinks?.let {
                            // Use thumbnail for grid view to save data
                            it.thumbnail?.replace("http:", "https:")
                                ?: it.smallThumbnail?.replace("http:", "https:")
                        })
                        .crossfade(true)
                        .build(),
                    contentDescription = book.volumeInfo.title,
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = book.volumeInfo.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                book.volumeInfo.authors?.let { authors ->
                    Row(
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Author(s): ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = authors.joinToString(", "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                book.volumeInfo.publisher?.let { publisher ->
                    Row(
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Publisher: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = publisher,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onImeSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search)
            )
        },
        label = { Text(stringResource(R.string.search)) },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onImeSearch() }
        ),
        trailingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isNotBlank()
            ) {
                IconButton(
                    onClick = {
                        onQueryChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_hint),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
}

@Composable
fun SearchBarWithBack(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        leadingIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        label = { Text("Search Results") },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
        trailingIcon = {
            IconButton(onClick = {
                onSearch()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }
    )
}

@Composable
fun EmptySearchScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.enter_search_term),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
