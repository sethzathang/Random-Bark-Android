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
 * Repository class responsible to grabbing the data from the service
 */
class RandomDogRepositoryImpl @Inject constructor(
    private val network: NetworkService
) : RandomDogRepository {

    /**
     * Fetch with Coroutine
     *
     * [runCatching] - similar to try/catch, it executes the block of code and catches any exceptions.
     * [fold]- processes the result of runCatching
     * [flowOn] - ensures the network call is performed on the IO to avoid blocking the main thread
     * since network operations can take a long time to complete and performing on the main thread can block the UI.
     * It also allows multiple network requests or other IO operations to run concurrently in the background
     * without interfering with the main thread.
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
     * Fetches a random dog image using RxJava.
     * @return An Observable emitting ServiceResult containing the RandomDogImageReply.
     */
    override fun getRandomDogImageWithRxJava(): Observable<ServiceResult<RandomDogImageReply>> =
        // Fetches a random dog image from the network as an Observable
        network.fetchRandomDogImageWithRxJava()
            // Converts the result to an Observable to handle asynchronous operations effectively
            .toObservable()
            // Maps the result to handle network results
            .map { result -> result.handleNetworkResults() }
            // Handles any errors by returning a ServiceResult.Error
            .onErrorReturn { exception -> ServiceResult.Error(exception) }
            // Starts the Observable sequence with a loading state
            .startWith(ServiceResult.Loading)
            // Specifies that the Observable should operate on the I/O scheduler
            // ensuring these tasks are performed on a background thread
            .subscribeOn(Schedulers.io())
            // Observes the result on the main thread
            .observeOn(AndroidSchedulers.mainThread())

    /**
     * Extension function to handle network results.
     * @return A ServiceResult indicating success or error based on the status of the response.
     */
    private fun RandomDogImageReply.handleNetworkResults() =
        if (this.status.equals(other = "success", ignoreCase = true)) {
            ServiceResult.Success(result = this)
        } else {
            ServiceResult.Error(Throwable("Invalid response status"))
        }
}
