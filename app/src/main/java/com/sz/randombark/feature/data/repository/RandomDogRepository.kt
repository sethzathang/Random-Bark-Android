package com.sz.randombark.feature.data.repository

import com.sz.randombark.common.ServiceResult
import com.sz.randombark.feature.data.model.RandomDogImageReply
import kotlinx.coroutines.flow.Flow

interface RandomDogRepository {
    suspend fun getRandomDogImage(): Flow<ServiceResult<RandomDogImageReply>>
}
