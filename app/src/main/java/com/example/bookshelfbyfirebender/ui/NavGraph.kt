package com.example.bookshelfbyfirebender.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookshelfbyfirebender.network.Book
import com.example.bookshelfbyfirebender.ui.screens.BookDetailsScreen
import com.example.bookshelfbyfirebender.ui.screens.BookshelfHomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details")
}

@Composable
fun BookshelfNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            BookshelfHomeScreen(
                onBookClick = { book ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("book", book)
                    navController.navigate(Screen.Details.route)
                }
            )
        }
        composable(Screen.Details.route) {
            val book = navController.previousBackStackEntry?.savedStateHandle?.get<Book>("book")
            book?.let {
                BookDetailsScreen(book = it)
            }
        }
    }
}