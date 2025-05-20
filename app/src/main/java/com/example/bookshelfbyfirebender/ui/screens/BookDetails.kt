package com.example.bookshelfbyfirebender.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelfbyfirebender.network.Book
import com.example.bookshelfbyfirebender.ui.screens.BookDetailsViewModel
import com.example.bookshelfbyfirebender.ui.screens.BookDetailsState

//sealed interface BookDetailsState {
//    data class Success(val book: Book) : BookDetailsState
//    object Error : BookDetailsState
//    object Loading : BookDetailsState
//}

//class BookDetailsViewModel : ViewModel() {
//    var bookDetailsState: BookDetailsState by mutableStateOf(BookDetailsState.Loading)
//        private set
//
//    fun getBookDetails(book: Book) {
//        bookDetailsState = BookDetailsState.Success(book) // Show initial book data immediately
//
//        viewModelScope.launch {
//            try {
//                val detailedBook = BookApi.retrofitService.getBookDetails(book.selfLink)
//                bookDetailsState = BookDetailsState.Success(detailedBook)
//            } catch (e: IOException) {
//                // If we fail to get detailed info, keep showing the original book
//            } catch (e: Exception) {
//                // If we fail to get detailed info, keep showing the original book
//            }
//        }
//    }
//}

@Composable
fun BookDetailsScreen(
    book: Book,
    modifier: Modifier = Modifier
) {
    val viewModel: BookDetailsViewModel = viewModel()
    
    // Initialize with the book
    LaunchedEffect(book.id) {
        if (book.selfLink.isNotEmpty()) {
            viewModel.getBookDetails(book)
        }
    }
    
    when (val state = viewModel.bookDetailsState) {
        is BookDetailsState.Loading -> {
            // Show loading indicator
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is BookDetailsState.Success -> {
            DetailContent(book = state.book, modifier = modifier)
        }
        is BookDetailsState.Error -> {
            // Fallback to using the original book data
            DetailContent(book = book, modifier = modifier)
        }
    }
}

@Composable
fun DetailContent(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Choose the best available image, from highest to lowest resolution
        val imageUrl = with(book.volumeInfo.imageLinks) {
            this?.extraLarge?.replace("http:", "https:")
                ?: this?.large?.replace("http:", "https:")
                ?: this?.medium?.replace("http:", "https:")
                ?: this?.small?.replace("http:", "https:")
                ?: this?.thumbnail?.replace("http:", "https:") 
                ?: ""
        }
        
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = book.volumeInfo.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )
        
        Text(
            text = book.volumeInfo.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        book.volumeInfo.authors?.let { authors ->
            Text(
                text = "Author(s): ${authors.joinToString(", ")}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        
        book.volumeInfo.publisher?.let { publisher ->
            Text(
                text = "Publisher: $publisher",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}