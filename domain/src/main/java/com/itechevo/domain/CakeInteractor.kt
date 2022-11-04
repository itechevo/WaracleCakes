package com.itechevo.domain

import com.itechevo.domain.model.Cake
import com.itechevo.domain.model.NetworkResult
import com.itechevo.domain.repository.CakeRepository
import com.itechevo.domain.usecase.CakeUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class CakeInteractor(
    private val cakeRepository: CakeRepository,
    private val dispatcher: CoroutineDispatcher
) : CakeUseCase {

    override suspend fun getOrderedCakes(): Flow<NetworkResult<List<Cake>>> = flow {
        cakeRepository.getCakes().collect { response ->
            val result = when (response) {
                is NetworkResult.Error -> response
                is NetworkResult.Success -> {
                    response.data.distinct().sortedBy { it.title }.let {
                        NetworkResult.Success(it)
                    }
                }
            }
            emit(result)
        }
    }.flowOn(dispatcher)
}