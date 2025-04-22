package com.example.bookshelfbyfirebender.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class BookShelf(
    val items: List<Book>,
    val kind: String,
    val totalItems: Int
) : Parcelable

@Parcelize
@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
) : Parcelable

@Parcelize
@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = null,
    val publisher: String? = null,
    @SerialName(value = "imageLinks")
    val imageLinks: ImageLinks? = null
) : Parcelable

@Parcelize
@Serializable
data class ImageLinks(
    val thumbnail: String
) : Parcelable