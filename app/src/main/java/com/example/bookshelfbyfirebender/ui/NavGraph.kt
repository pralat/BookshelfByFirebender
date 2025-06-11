package com.example.bookshelfbyfirebender.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookshelfbyfirebender.network.Book
import com.example.bookshelfbyfirebender.ui.screens.BookDetailsScreen
import com.example.bookshelfbyfirebender.ui.screens.BookshelfHomeScreen
import com.example.bookshelfbyfirebender.ui.screens.BookshelfSearchResultsScreen
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SearchResults : Screen("search_results/{query}") {
        fun createRoute(query: String) = "search_results/$query"
    }
    object Details : Screen("details")
}

@Composable
fun BookshelfNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            BookshelfHomeScreen(
                onSearch = { query ->
                    navController.navigate(Screen.SearchResults.createRoute(query))
                }
            )
        }
        composable(
            route = Screen.SearchResults.route,
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) {
            val query = it.arguments?.getString("query") ?: ""
            BookshelfSearchResultsScreen(
                searchQuery = query,
                onBookClick = { book ->
                    println("Navigating to details for book: ${book.volumeInfo.title}")
                    navController.currentBackStackEntry?.savedStateHandle?.set("book", book)
                    navController.navigate(Screen.Details.route)
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Details.route) {
            val book = navController.previousBackStackEntry?.savedStateHandle?.get<Book>("book")
            if (book != null) {
                BookDetailsScreen(
                    book = book,
                    onBackClick = {
                        println("Back button clicked from Details")
                        navController.popBackStack()
                    }
                )
            } else {
                // Fallback if book data is not available
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Book data not found")
                }
            }
        }
    }
}
