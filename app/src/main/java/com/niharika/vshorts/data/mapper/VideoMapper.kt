package com.niharika.vshorts.data.mapper

import com.niharika.vshorts.data.response.VideoResponse
import com.niharika.vshorts.domain.model.VideoModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun VideoResponse.toVideoList(): List<VideoModel> {
    return items.map { videoItem ->
        VideoModel(
            videoId = videoItem.id.videoId,
            title = videoItem.snippet.title,
            thumbnailUrl = videoItem.snippet.thumbnails.high.url,
            channelTitle = videoItem.snippet.channelTitle,
            publishTime = formatDate( videoItem.snippet.publishTime)
        )
    }
}

fun formatDate(dateString: String): String {
    try {
        // Parse the input date string into a Date object
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)

        // Format the Date into the desired output format
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        return outputFormat.format(date)

    } catch (e: Exception) {
        e.printStackTrace() // Handle parsing exception if necessary
        return "Invalid Date"
    }
}