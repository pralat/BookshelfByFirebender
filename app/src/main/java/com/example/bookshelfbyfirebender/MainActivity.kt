package com.example.bookshelfbyfirebender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.bookshelfbyfirebender.ui.BookshelfNavGraph
import com.example.bookshelfbyfirebender.ui.theme.BookshelfByFirebenderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookshelfByFirebenderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BookshelfNavGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
