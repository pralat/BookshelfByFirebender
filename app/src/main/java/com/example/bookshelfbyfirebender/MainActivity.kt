package com.example.bookshelfbyfirebender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bookshelfbyfirebender.ui.screens.BookshelfHomeScreen
import com.example.bookshelfbyfirebender.ui.theme.BookshelfByFirebenderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookshelfByFirebenderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BookshelfApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BookshelfApp(modifier: Modifier = Modifier) {
    BookshelfHomeScreen(modifier = modifier)
}
