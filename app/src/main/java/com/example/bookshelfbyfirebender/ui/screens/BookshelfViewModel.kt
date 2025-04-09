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

sealed interface BookshelfUiState {
    data class Success(val books: List<Book>) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
}

class BookshelfViewModel : ViewModel() {
    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    init {
        getBooks()
    }

    fun getBooks(query: String = "fiction") {
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            bookshelfUiState = try {
                val result = BookApi.retrofitService.getBooks(query)
                BookshelfUiState.Success(result.items)
            } catch (e: IOException) {
                BookshelfUiState.Error
            } catch (e: Exception) {
                BookshelfUiState.Error
            }
        }
    }
}