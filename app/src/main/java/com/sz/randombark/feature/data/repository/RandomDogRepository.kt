package com.sz.randombark.feature.data.repository

import com.sz.randombark.module.NetworkResult
import com.sz.randombark.feature.data.model.RandomDogImageReply
import kotlinx.coroutines.flow.Flow

interface RandomDogRepository {
    suspend fun getRandomDogImage(): Flow<NetworkResult<RandomDogImageReply>>
}
