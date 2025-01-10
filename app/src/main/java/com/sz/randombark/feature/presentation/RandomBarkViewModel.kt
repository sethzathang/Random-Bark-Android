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
                                breed = extractAndFormatDogBreed(
                                    imageUrl = result.result.imageUrl
                                ),
                                image = result.result.imageUrl
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * Find and extract the dog breed name between "breeds/" and the next "/" in the given image URL.
     * If no match is found, it returns "Breed Information Unavailable".
     * If the breed name contains a dash, it splits the name into separate words,
     * capitalizes the first letter of each word, and joins them with a space.
     *
     * @param imageUrl The URL of the dog image
     * @return The formatted dog breed name
     */
    private fun extractAndFormatDogBreed(imageUrl: String): String {
        val pattern = """breeds/([^/]+)/""".toRegex()
        val result = pattern.find(imageUrl)?.groups?.get(1)?.value ?: "Breed Information Unavailable"
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
