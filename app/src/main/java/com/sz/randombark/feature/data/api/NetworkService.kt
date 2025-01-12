package com.sz.randombark.feature.data.api

import com.sz.randombark.feature.data.model.RandomDogImageReply
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Interface for making network requests.
 */
interface NetworkService {
    /**
     * Fetches a random dog image using Kotlin Coroutines.
     *
     * The function is marked with `suspend`, indicating that it should be called
     * from within a coroutine or another suspend function.
     */
    @GET("/api/breeds/image/random")
    suspend fun fetchRandomDogImageWithCoroutine(): RandomDogImageReply

    /**
     * Fetches a random dog image using RxJava.
     *
     * User Single instead of Observable since network call will result in a single response or an error.
     */
    @GET("/api/breeds/image/random")
    fun fetchRandomDogImageWithRxJava(): Single<RandomDogImageReply>
}