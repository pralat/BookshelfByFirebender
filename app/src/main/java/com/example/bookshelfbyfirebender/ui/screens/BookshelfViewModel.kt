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
    object EmptySearch : BookshelfUiState
}

class BookshelfViewModel : ViewModel() {
    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.EmptySearch)
        private set
    
    var searchQuery by mutableStateOf("")
        private set

    init {
        getBooks()
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun searchBooks() {
        getBooks(searchQuery)
    }

    private fun getBooks(query: String = "") {
        if (query.isBlank()) {
            bookshelfUiState = BookshelfUiState.EmptySearch
            return
        }

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