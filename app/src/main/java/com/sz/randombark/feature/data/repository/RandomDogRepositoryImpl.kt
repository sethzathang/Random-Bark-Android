package com.sz.randombark.feature.data.repository

import com.sz.randombark.common.ServiceResult
import com.sz.randombark.feature.data.api.NetworkService
import com.sz.randombark.feature.data.model.RandomDogImageReply
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * [runCatching] - similar to try/catch, it executes the block of code and catches any exceptions.
 * [fold]- processes the result of runCatching
 * [flowOn] - ensures the network call is performed on the IO to avoid blocking the main thread
 * since network operations can take a long time to complete and performing on the main thread can block the UI.
 * It also allows multiple network requests or other IO operations to run concurrently in the background
 * without interfering with the main thread.
 */
class RandomDogRepositoryImpl @Inject constructor(
    private val network: NetworkService
) : RandomDogRepository {

    /**
     * Fetch with Coroutine
     */
    override suspend fun getRandomDogImageWithCoroutine(): Flow<ServiceResult<RandomDogImageReply>> =
        flow {
            // Emit a loading state before starting the network request
            emit(ServiceResult.Loading)
            // Use runCatching to handle success and error scenarios, can also do try/catch
            runCatching {
                network.fetchRandomDogImageWithCoroutine()
            }.fold(
                onSuccess = { result ->
                    if (result.status.equals(other = "success", ignoreCase = true)) {
                        emit(ServiceResult.Success(result))
                    } else {
                        emit(ServiceResult.Error(Throwable("Invalid response status")))
                    }
                },
                onFailure = { exception ->
                    emit(ServiceResult.Error(error = exception))
                }
            )
        }.flowOn(Dispatchers.IO)

    /**
     * Fetch with RxJava
     */
    override fun getRandomDogImageWithRxJava(): Observable<ServiceResult<RandomDogImageReply>> =
        network.fetchRandomDogImageWithRxJava()
            .toObservable()
            .map { result -> result.handleNetworkResults() }
            .onErrorReturn { exception -> ServiceResult.Error(exception) }
            .startWith(ServiceResult.Loading)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private fun RandomDogImageReply.handleNetworkResults() =
        if (this.status.equals(other = "success", ignoreCase = true)) {
            ServiceResult.Success(result = this)
        } else {
            ServiceResult.Error(Throwable("Invalid response status"))
        }
}
