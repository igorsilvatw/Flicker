package com.flicker.cvs.feature.flimage.data

data class FLImageResponse(
    val description: String,
    val generator: String,
    val items: List<Item>,
    val link: String,
    val modified: String,
    val title: String
)

data class Item(
    val author: String,
    val author_id: String,
    val date_taken: String,
    val description: String,
    val link: String,
    val media: Media,
    val published: String,
    val tags: String,
    val title: String
)

data class Media(
    val m: String
)
