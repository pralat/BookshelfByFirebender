package com.example.bookshelfbyfirebender.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelfbyfirebender.network.Book
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

@Composable
fun BookDetailsScreen(
    book: Book,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Check if user is on WiFi
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    val network = connectivityManager?.activeNetwork
    val capabilities = connectivityManager?.getNetworkCapabilities(network)
    val isWifi = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = context)
                .data(book.volumeInfo.imageLinks?.let {
                    if (isWifi) {
                        // On WiFi, use higher resolution images
                        it.large?.replace("http:", "https:")
                            ?: it.medium?.replace("http:", "https:")
                            ?: it.small?.replace("http:", "https:")
                            ?: it.thumbnail?.replace("http:", "https:")
                    } else {
                        // On mobile data, use medium resolution to save data
                        it.medium?.replace("http:", "https:")
                            ?: it.small?.replace("http:", "https:")
                            ?: it.thumbnail?.replace("http:", "https:")
                    }
                })
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