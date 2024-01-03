package edu.pwr.kozanecki.flickrapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotosViewModel(private val flickrApi: FlickrApi) : ViewModel() {
    var photos = mutableStateOf<FlickrResponse?>(null)
    var isLoading = mutableStateOf(false)

    init {
        fetchPhotos()
    }

    private fun fetchPhotos() {
        viewModelScope.launch {
            isLoading.value = true
            photos.value = getPublicPhotos()
            isLoading.value = false
        }
    }

    private suspend fun getPublicPhotos(): FlickrResponse? {
        val response: FlickrResponse? = try {
            flickrApi.getPublicPhotos()
        } catch (e: Exception) {
            null
        }
        return response
    }
}