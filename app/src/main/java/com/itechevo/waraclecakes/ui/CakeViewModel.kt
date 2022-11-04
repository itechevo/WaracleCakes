package com.itechevo.waraclecakes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itechevo.domain.model.Cake
import com.itechevo.domain.model.NetworkResult
import com.itechevo.domain.usecase.CakeUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CakeViewModel(private val useCase: CakeUseCase) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Content(val result: List<Cake>) : UiState()
        object Error : UiState()
    }

    private val _uiState =
        MutableSharedFlow<UiState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val uiState = _uiState.asSharedFlow()

    init {
        getCakes()
    }

    fun getCakes() {
        _uiState.tryEmit(UiState.Loading)
        viewModelScope.launch {
            useCase.getOrderedCakes().collect { result ->
                handleResult(result)
            }
        }
    }

    private fun handleResult(result: NetworkResult<List<Cake>>) {
        when (result) {
            is NetworkResult.Error -> _uiState.tryEmit(UiState.Error)
            is NetworkResult.Success -> _uiState.tryEmit(UiState.Content(result.data))
        }
    }
}