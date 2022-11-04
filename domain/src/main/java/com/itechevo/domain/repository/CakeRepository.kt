package com.itechevo.domain.repository

import com.itechevo.domain.model.Cake
import com.itechevo.domain.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CakeRepository {

    suspend fun getCakes(): Flow<NetworkResult<List<Cake>>>

}