package com.sz.randombark.feature.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sz.randombark.common.ServiceResult
import com.sz.randombark.common.ViewState
import com.sz.randombark.common.utils.extractAndFormatDogBreed
import com.sz.randombark.feature.data.repository.RandomDogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for fetching random dog images using Coroutine and RxJava.
 *
 * @property repository The repository to fetch the random dog images.
 */
@HiltViewModel
class RandomBarkViewModel @Inject constructor(
    private val repository: RandomDogRepository
) : ViewModel() {

    // StateFlow to hold the view state when using Coroutine
    private val _viewStateCoroutine = MutableStateFlow<ViewState<RandomDogUIModel>>(ViewState.Loading)
    val viewStateCoroutine: StateFlow<ViewState<RandomDogUIModel>> = _viewStateCoroutine

    // LiveData to hold the view state when using RxJava
    private val _viewStateRxJava = MutableLiveData<ViewState<RandomDogUIModel>>(ViewState.Loading)
    val viewStateRxJava: LiveData<ViewState<RandomDogUIModel>> get() = _viewStateRxJava

    // CompositeDisposable to manage RxJava subscriptions
    private val compositeDisposable = CompositeDisposable()

    /**
     * Clears the CompositeDisposable to avoid memory leaks.
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    /**
     * Fetches a random dog image using Coroutine.
     */
    fun fetchRandomDogCoroutine() {
        viewModelScope.launch {
            repository.getRandomDogImageWithCoroutine().collect { result ->
                when (result) {
                    is ServiceResult.Loading -> {
                        _viewStateCoroutine.value = ViewState.Loading
                    }

                    is ServiceResult.Error -> {
                        _viewStateCoroutine.value = ViewState.Error(
                            message = result.error.message ?: "Unknown Network Error"
                        )
                    }

                    is ServiceResult.Success -> {
                        _viewStateCoroutine.value = ViewState.Success(
                            data = RandomDogUIModel(
                                breed = result.result.imageUrl.extractAndFormatDogBreed(),
                                image = result.result.imageUrl
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * Fetches a random dog image using RxJava.
     */
    fun fetchRandomDogRxJava() {
        //adding the subscription to the CompositeDisposable ensures that the
        //subscription is properly managed and can be disposed of to prevent memory leaks.
        compositeDisposable.add(
            //subscribes to the Observable returned by repository
            repository.getRandomDogImageWithRxJava().subscribe(
                { result ->
                    when (result) {
                        is ServiceResult.Loading -> {
                            _viewStateRxJava.value = ViewState.Loading
                        }

                        is ServiceResult.Error -> {
                            _viewStateRxJava.value = ViewState.Error(
                                message = result.error.message ?: "Unknown Network Error"
                            )
                        }

                        is ServiceResult.Success -> {
                            _viewStateRxJava.value = ViewState.Success(
                                data = RandomDogUIModel(
                                    breed = result.result.imageUrl.extractAndFormatDogBreed(),
                                    image = result.result.imageUrl
                                )
                            )
                        }
                    }
                },
                { error ->
                    // Handle any errors emitted by the Observable
                    _viewStateRxJava.value = ViewState.Error(
                        message = error.message ?: "Unknown Network Error"
                    )
                }
            )
        )
    }
}
