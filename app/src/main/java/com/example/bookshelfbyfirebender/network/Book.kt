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
    val selfLink: String = "" // Default value for compatibility
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
    val smallThumbnail: String? = null,
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("small")
    val small: String? = null,
    @SerialName("medium")
    val medium: String? = null,
    @SerialName("large")
    val large: String? = null,
    @SerialName("extraLarge")
    val extraLarge: String? = null
) : Parcelable