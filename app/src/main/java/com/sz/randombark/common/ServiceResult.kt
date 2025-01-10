package com.sz.randombark.common

/**
 * Define a sealed class to represent different states of network results
 */
sealed class ServiceResult<out ResponseType> {
    data class Success<out ResponseType>(val result: ResponseType) : ServiceResult<ResponseType>()
    data class Error(val error: Throwable) : ServiceResult<Nothing>()
    data object Loading : ServiceResult<Nothing>()
}
