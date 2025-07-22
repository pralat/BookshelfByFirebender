package com.example.bookshelfbyfirebender.ui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.bookshelfbyfirebender.network.BookApiService
import com.example.bookshelfbyfirebender.network.Book
import com.example.bookshelfbyfirebender.network.BookApi
import com.example.bookshelfbyfirebender.network.BookShelf
import com.example.bookshelfbyfirebender.network.VolumeInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookshelfViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeApi: BookApiService
    private lateinit var viewModel: BookshelfViewModel

    @Before
    fun setUp() {
        // Redirect main dispatcher to the test dispatcher for test isolation
        Dispatchers.setMain(testDispatcher)
        // Fake API for testing
        fakeApi = object : BookApiService {
            override suspend fun getBooks(
                query: String,
                startIndex: Int,
                maxResults: Int
            ): BookShelf {
                return BookShelf(
                    items = listOf(
                        Book(
                            id = "1",
                            volumeInfo = VolumeInfo(title = "Test Book"),
                            selfLink = ""
                        )
                    ),
                    kind = "books#volumes",
                    totalItems = 1
                )
            }

            override suspend fun getBookDetails(url: String): Book {
                error("not needed")
            }
        }

        BookApi.retrofitService = fakeApi // Override service with fake API
        viewModel = BookshelfViewModel()
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to the original Main dispatcher for the JVM
        Dispatchers.resetMain()
    }

    @Test
    fun test_searchBooks_setsSuccessState() = runTest(testDispatcher) {
        viewModel.updateSearchQuery("test")
        viewModel.searchBooks()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.bookshelfUiState // Renamed to avoid confusion
        assertTrue(uiState is BookshelfUiState.Success)
        val successState = uiState as BookshelfUiState.Success // Use a new variable
        assertEquals(1, successState.books.size)
        assertEquals("Test Book", successState.books[0].volumeInfo.title)
    }
}
