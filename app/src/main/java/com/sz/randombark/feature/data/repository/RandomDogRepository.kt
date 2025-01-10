package com.sz.randombark.feature.data.repository

import com.sz.randombark.common.ServiceResult
import com.sz.randombark.feature.data.model.RandomDogImageReply
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface RandomDogRepository {
    suspend fun getRandomDogImageWithCoroutine(): Flow<ServiceResult<RandomDogImageReply>>
    fun getRandomDogImageWithRxJava(): Observable<ServiceResult<RandomDogImageReply>>
}
