package com.flicker.cvs.feature.flimage.presentation

import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.flicker.cvs.feature.flimage.domain.FLImage


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    flickerViewModel: FlickerViewModel = viewModel()
) {
    val flickerData by flickerViewModel.uiState.collectAsState()
    val flImageList = (flickerData as? StateSuccess)?.data ?: emptyList()
    var query by rememberSaveable { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = { text ->
                flickerViewModel.fetchFlickerData(text)
                query = text
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            ),
            placeholder = {
                Text("Search")
            },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(8.dp)

        )
        when (flickerData) {
            is StateSuccess -> {
                ImagesGridScreen(flImageList)
            }

            is StateError -> {
                ErrorContent()
            }

            is StateLoading -> {
                LoadingContent()
            }
        }
    }

}

@Composable
private fun LoadingContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(text = "Server error")
    }
}

@Composable
fun ImagesGridScreen(images: List<FLImage> = emptyList()) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(images) { image ->
                var show by rememberSaveable { mutableStateOf(false) }
                Column(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .padding(8.dp)
                        .border(
                            1.dp,
                            Color.Gray.copy(alpha = 0.5f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image.url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .heightIn(80.dp)
                            .widthIn(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = {
                                show = !show
                            })
                    )
                    val density = LocalDensity.current
                    AnimatedVisibility(
                        visible = show,
                        enter = slideInVertically {
                            // Slide in from 40 dp from the top.
                            with(density) { -40.dp.roundToPx() }
                        } + slideInHorizontally {
                            // Slide in from 40 dp from the top.
                            with(density) { -40.dp.roundToPx() }
                        } + expandHorizontally(
                            expandFrom = Alignment.Start
                        ) + expandVertically(
                            // Expand from the top.
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            // Fade in with the initial alpha of 0.3f.
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        Column {
                            Text(text = image.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(text = image.author, fontSize = 12.sp)
                            Text(text = image.publishedDate, fontSize = 12.sp)
                            HtmlText(html = image.description)
                        }
                    }
                }

            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = {
            it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)
        }
    )
}
