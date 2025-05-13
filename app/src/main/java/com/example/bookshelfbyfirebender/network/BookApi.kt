package com.example.bookshelfbyfirebender.network

//import androidx.privacysandbox.tools.core.generator.build
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import okhttp3.OkHttpClient // Import OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // Import HttpLoggingInterceptor

private const val BASE_URL = "https://www.googleapis.com/books/v1/"

private val json = Json { ignoreUnknownKeys = true }

// Create a logging interceptor
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY // Set the level to BODY for detailed logging
}

// Create an OkHttpClient and add the interceptor
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(okHttpClient) // Use the OkHttpClient with the interceptor
    .build()

object BookApi {
    val retrofitService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}