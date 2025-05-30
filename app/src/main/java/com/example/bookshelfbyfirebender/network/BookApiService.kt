package com.example.bookshelfbyfirebender.network

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface BookApiService {
    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): BookShelf
    
    @GET
    suspend fun getBookDetails(@Url url: String): Book
}