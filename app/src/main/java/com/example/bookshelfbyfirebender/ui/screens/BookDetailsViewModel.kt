package com.example.bookshelfbyfirebender.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfbyfirebender.network.Book
import com.example.bookshelfbyfirebender.network.BookApi
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface BookDetailsState {
    data class Success(val book: Book) : BookDetailsState
    object Error : BookDetailsState
    object Loading : BookDetailsState
}

class BookDetailsViewModel : ViewModel() {
    var bookDetailsState: BookDetailsState by mutableStateOf(BookDetailsState.Loading)
        private set

    fun getBookDetails(book: Book) {
        bookDetailsState = BookDetailsState.Success(book) // Show initial book data immediately

        viewModelScope.launch {
            try {
                val detailedBook = BookApi.retrofitService.getBookDetails(book.selfLink)
                bookDetailsState = BookDetailsState.Success(detailedBook)
            } catch (e: IOException) {
                // If we fail to get detailed info, keep showing the original book
            } catch (e: Exception) {
                // If we fail to get detailed info, keep showing the original book
            }
        }
    }
}