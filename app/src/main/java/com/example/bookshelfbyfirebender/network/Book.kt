package com.example.bookshelfbyfirebender.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookShelf(
    val items: List<Book>,
    val kind: String,
    val totalItems: Int
)

@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = null,
    val publisher: String? = null,
    @SerialName(value = "imageLinks")
    val imageLinks: ImageLinks? = null
)

@Serializable
data class ImageLinks(
    val thumbnail: String
)