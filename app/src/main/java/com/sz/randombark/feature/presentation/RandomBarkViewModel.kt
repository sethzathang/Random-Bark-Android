package com.sz.randombark.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sz.randombark.common.ServiceResult
import com.sz.randombark.common.ViewState
import com.sz.randombark.feature.data.repository.RandomDogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomBarkViewModel @Inject constructor(
    private val repository: RandomDogRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState<RandomDogUIModel>>(ViewState.Loading)
    val viewState: StateFlow<ViewState<RandomDogUIModel>> = _viewState

    init {
        fetchRandomDog()
    }

    fun fetchRandomDog() {
        viewModelScope.launch {
            repository.getRandomDogImage().collect { result ->
                when (result) {
                    is ServiceResult.Loading -> {
                        _viewState.value = ViewState.Loading
                    }

                    is ServiceResult.Error -> {
                        _viewState.value = ViewState.Error(
                            message = result.error.message ?: "Unknown Network Error"
                        )
                    }

                    is ServiceResult.Success -> {
                        _viewState.value = ViewState.Success(
                            data = RandomDogUIModel(
                                breed = extractAndFormatDogBreed(imageUrl = result.result.imageUrl),
                                imageUrl = result.result.imageUrl
                            )
                        )
                    }
                }
            }
        }
    }

    private fun extractAndFormatDogBreed(imageUrl: String): String {
        val pattern = """/breeds/([^/]+)/""".toRegex()
        val result = pattern.find(imageUrl)?.groups?.get(1)?.value ?: "Other"
        return result.split("-").joinToString(" ") { word ->
            word.replaceFirstChar { data ->
                if (data.isLowerCase()) {
                    data.titlecase()
                } else {
                    data.toString()
                }
            }
        }
    }
}
