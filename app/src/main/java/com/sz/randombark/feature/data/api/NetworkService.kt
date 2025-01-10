package com.sz.randombark.feature.data.api

import com.sz.randombark.feature.data.model.RandomDogImageReply
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Interface for making network requests.
 */
interface NetworkService {
    @GET("/api/breeds/image/random")
    suspend fun fetchRandomDogImageWithCoroutine(): RandomDogImageReply

    @GET("/api/breeds/image/random")
    fun fetchRandomDogImageWithRxJava(): Single<RandomDogImageReply>
}