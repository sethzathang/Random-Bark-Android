package com.sz.randombark.common

sealed class ViewState<out Type> {
    data object Loading : ViewState<Nothing>()
    data class Success<out Type>(val data: Type) : ViewState<Type>()
    data class Error(val message: String) : ViewState<Nothing>()
}
