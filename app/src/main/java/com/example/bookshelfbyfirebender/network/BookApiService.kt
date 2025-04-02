package com.example.bookshelfbyfirebender.network

import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): BookShelf
}