package com.flicker.cvs.feature.flimage.domain

import com.flicker.cvs.feature.flimage.data.FLImageResponse
import com.flicker.cvs.feature.flimage.data.Item
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun FLImageResponse?.toDomain(): List<FLImage> {
    val flImageList = mutableListOf<FLImage>()
    this?.items?.forEach {
        flImageList.add(it.toFLImage())
    }
    return flImageList
}

fun Item.toFLImage(): FLImage {
    return FLImage(
        url = this.media.m,
        title = this.title,
        description = this.description,
        publishedDate = this.published.printPublishDate(),
        author = this.author
    )
}

private fun String.printPublishDate(): String {
    val instant = Instant.parse(this)
    val zoneId = ZoneId.systemDefault()

    val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return localDateTime.format(formatter)
}