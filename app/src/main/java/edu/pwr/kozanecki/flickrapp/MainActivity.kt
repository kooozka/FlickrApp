package edu.pwr.kozanecki.flickrapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import edu.pwr.kozanecki.flickrapp.ui.theme.FlickrAppTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = provideRetrofit()
        val flickrApi = retrofit.create(FlickrApi::class.java)
        val viewModel = PhotosViewModel(flickrApi)
        setContent {
            FlickrAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PhotoList(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PhotoCard(photo: Photo) {
    Column {
        Text(
            text = photo.title,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge
        )
        Image(
            painter = rememberImagePainter(data = photo.media.m),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "author: " + photo.author,
            modifier = Modifier
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}

@Composable
fun PhotoList(viewModel: PhotosViewModel) {
    val photos: FlickrResponse? = viewModel.photos.value
    val isLoading = viewModel.isLoading.value

    Column {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                if (photos != null) {
                    items(photos.items.size) { index ->
                        PhotoCard(photos.items[index])
                    }
                }
            }
        }
    }
}

private fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.flickr.com/services/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}