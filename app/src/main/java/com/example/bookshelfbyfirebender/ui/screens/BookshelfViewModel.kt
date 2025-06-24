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
    data class Success(
        val books: List<Book>,
        val totalItems: Int,
        val startIndex: Int,
        val isLoadingMore: Boolean = false
    ) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
    object EmptySearch : BookshelfUiState
}

class BookshelfViewModel : ViewModel() {
    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.EmptySearch)
        private set
    
    var searchQuery by mutableStateOf("")
        private set

    private val maxResults = 20 // Books per page
    private var currentBooks = mutableListOf<Book>()

    init {
        getBooks()
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        // If the query is cleared, reset to empty search state
        if (query.isBlank()) {
            bookshelfUiState = BookshelfUiState.EmptySearch
            currentBooks.clear()
        }
    }

    fun searchBooks() {
        currentBooks.clear()
        getBooks(searchQuery, startIndex = 0)
    }

    fun loadMoreBooks() {
        val currentState = bookshelfUiState
        if (currentState is BookshelfUiState.Success && !currentState.isLoadingMore) {
            val nextStartIndex = currentState.startIndex + maxResults
            if (nextStartIndex < currentState.totalItems) {
                bookshelfUiState = currentState.copy(isLoadingMore = true)
                getBooks(searchQuery, startIndex = nextStartIndex, isLoadingMore = true)
            }
        }
    }

    private fun getBooks(query: String = "", startIndex: Int = 0, isLoadingMore: Boolean = false) {
        if (query.isBlank()) {
            bookshelfUiState = BookshelfUiState.EmptySearch
            return
        }

        if (!isLoadingMore) {
            bookshelfUiState = BookshelfUiState.Loading
        }

        viewModelScope.launch {
            bookshelfUiState = try {
                val result = BookApi.retrofitService.getBooks(query, startIndex, maxResults)
                
                if (isLoadingMore) {
                    currentBooks.addAll(result.items)
                } else {
                    currentBooks.clear()
                    currentBooks.addAll(result.items)
                }
                
                BookshelfUiState.Success(
                    books = currentBooks.toList(),
                    totalItems = result.totalItems,
                    startIndex = startIndex,
                    isLoadingMore = false
                )
            } catch (e: IOException) {
                if (isLoadingMore) {
                    // Keep current state but remove loading indicator
                    val currentState = bookshelfUiState as? BookshelfUiState.Success
                    currentState?.copy(isLoadingMore = false) ?: BookshelfUiState.Error
                } else {
                    BookshelfUiState.Error
                }
            } catch (e: Exception) {
                if (isLoadingMore) {
                    val currentState = bookshelfUiState as? BookshelfUiState.Success
                    currentState?.copy(isLoadingMore = false) ?: BookshelfUiState.Error
                } else {
                    BookshelfUiState.Error
                }
            }
        }
    }
}
