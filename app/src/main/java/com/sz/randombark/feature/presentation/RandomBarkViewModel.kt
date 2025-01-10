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

@HiltViewModel
class RandomBarkViewModel @Inject constructor(
    private val repository: RandomDogRepository
) : ViewModel() {

    private val _viewStateCoroutine =
        MutableStateFlow<ViewState<RandomDogUIModel>>(ViewState.Loading)
    val viewStateCoroutine: StateFlow<ViewState<RandomDogUIModel>> = _viewStateCoroutine

    private val _viewStateRxJava = MutableLiveData<ViewState<RandomDogUIModel>>(ViewState.Loading)
    val viewStateRxJava: LiveData<ViewState<RandomDogUIModel>> get() = _viewStateRxJava

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

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

    fun fetchRandomDogRxJava() {
        compositeDisposable.add(
            repository.getRandomDogImageWithRxJava()
                .subscribe({ result ->
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
                }, { error ->
                    _viewStateRxJava.value = ViewState.Error(
                        message = error.message ?: "Unknown Network Error"
                    )
                })
        )
    }
}
