package edu.pwr.kozanecki.flickrapp

import retrofit2.http.GET

interface FlickrApi {
    @GET("feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getPublicPhotos(): FlickrResponse
}