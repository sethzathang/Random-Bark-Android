package com.sz.randombark.module

import com.sz.randombark.feature.data.api.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Define a sealed class to represent different states of network results
 */
sealed class NetworkResult<out ResponseType> {
    data class Success<out ResponseType>(val result: ResponseType) : NetworkResult<ResponseType>()
    data class Error(val error: Throwable) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}

/**
 * Dagger module to provide the [NetworkService] instance.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of [NetworkService] using Retrofit.
     *
     * This function sets up Retrofit with a base URL, a Gson converter, and creates an instance
     * of [NetworkService] to be used for API calls.
     *
     * @return A singleton instance of [NetworkService].
     */
    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService = Retrofit
        .Builder()
        .baseUrl("https://dog.ceo")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NetworkService::class.java)
}
