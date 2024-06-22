package com.niharika.vshorts.presentation.home

import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.niharika.vshorts.R
import com.niharika.vshorts.data.response.VideoItem
import com.niharika.vshorts.domain.model.Resource
import com.niharika.vshorts.domain.model.VideoModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import java.util.TimeZone

@Composable
fun VideosScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar ={ YouTubeActionBar()}
    ) {
        Box(modifier = Modifier.padding(it))
        VideoList(it)
    }
}


@Composable
fun VideoList(paddingValues: PaddingValues,viewModel: VideoViewModel = hiltViewModel()) {
    val videosState = viewModel.videosState.collectAsState()
    when (val resource = videosState.value) {
        is Resource.Success<*> -> {
            val videos = (resource as Resource.Success<List<VideoModel>>).data
            LazyColumn(
                contentPadding = paddingValues // Apply padding values here
            ) {
                items(videos.chunked(5)) { videoChunk ->
            Column {
                // Display first two videos vertically stacked
                if (videoChunk.isNotEmpty()) {
                    videoChunk.take(2).forEachIndexed { index, video ->
                        VideoItem(video = video) {
                            // Handle video click if needed
                        }
                        if (index == 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                // Display next three videos horizontally
                if (videoChunk.size > 2) {
                    ShortsTextWithIcon(drawableResId = R.drawable.youtube_shorts_icon)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        items(videoChunk.drop(2)) { video ->
                            HVideoItem(video = video) {
                                // Handle video click if needed
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Adjust as needed
        }
    }

        }
        is Resource.Loading -> {
            LoadingScreen()
        }
        is Resource.Error -> {
            val errorMessage = resource.errorMessage
            ErrorScreen(errorMessage = errorMessage) { viewModel.getVideos() }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoItem(video: VideoModel,onItemClick: (VideoModel) -> Unit) {
    Card(
        shape = RectangleShape, // No rounded corners
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),

        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()

    ) {
        GlideImage(
            model = video.thumbnailUrl,
            contentDescription = null,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
                //.clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            GlideImage(
                model = video.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    Text(
                        text = video.channelTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = video.publishTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
        }
    }
}
@Composable
fun ShortsTextWithIcon(
    modifier: Modifier = Modifier,
    drawableResId: Int, // Pass drawable resource ID
    text: String = "Shorts"
) {
    val painter = painterResource(drawableResId) // Load the drawable as Painter

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HVideoItem(video: VideoModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp) // Set a specific width, adjust as needed
            .height(240.dp)
            //.padding(end = 2.dp)
            .clickable { onClick() },
        ) {

            Box(
                modifier = Modifier
                   .fillMaxSize()
                    //.padding(8.dp)
            ) {
                GlideImage(
                    model = video.thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.BottomStart)
                    )
                        {

                    Text(
                        text = video.title,

                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        modifier = Modifier
                    )
                    Row {
                        Text(
                            text = video.channelTitle,
                            fontSize = 10.sp,
                            maxLines = 1,
                            color = Color.Gray,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(errorMessage: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = errorMessage, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onRetry() }) {
                Text(text = "Retry")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeActionBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.ic_action_play), // Add your YouTube logo here
                    contentDescription = "YouTube Logo",
                    modifier = Modifier.width(40.dp).height(30.dp) // Adjust size as needed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "YouTube")
            }
        },
        actions = {
            IconButton(onClick = { /* Handle search icon click */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = { /* Handle notifications icon click */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
            IconButton(onClick = { /* Handle profile icon click */ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
            }
        }
    )
}

