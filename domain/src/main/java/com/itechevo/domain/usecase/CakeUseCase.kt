package com.itechevo.domain.usecase

import com.itechevo.domain.model.Cake
import com.itechevo.domain.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CakeUseCase {

    /**
     * Get cakes ordered by name with duplicate entries removed
     */
    suspend fun getOrderedCakes(): Flow<NetworkResult<List<Cake>>>
}