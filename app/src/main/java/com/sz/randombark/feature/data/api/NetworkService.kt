package com.sz.randombark.feature.data.api

import com.sz.randombark.feature.data.model.RandomDogImageReply
import retrofit2.http.GET

/**
 * Interface for making network requests.
 */
interface NetworkService {
    @GET("/api/breeds/image/random")
    suspend fun fetchRandomDogImage(): RandomDogImageReply
}