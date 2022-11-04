package com.itechevo.data

import androidx.collection.LruCache
import com.itechevo.data.mapper.toDomain
import com.itechevo.data.source.WaracleApiService
import com.itechevo.domain.model.Cake
import com.itechevo.domain.model.NetworkResult
import com.itechevo.domain.repository.CakeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CakeRepositoryImpl(
    private val waracleApiService: WaracleApiService,
    private val dispatcher: CoroutineDispatcher
) : CakeRepository {

    private val cache = LruCache<String, NetworkResult<List<Cake>>>(1)

    override suspend fun getCakes(): Flow<NetworkResult<List<Cake>>> = flow {
        try {
            //emit if we have cached response
            cache[CAKE_CACHE_KEY]?.let { result ->
                emit(result)
            }

            //request api response
            val cakes = waracleApiService.getCakes().map { it.toDomain() }

            val result = NetworkResult.Success(cakes)
            cache.put(CAKE_CACHE_KEY, result)

            emit(result)
        } catch (t: Throwable) {
            emit(NetworkResult.Error(t))
        }
    }.flowOn(dispatcher)

    companion object {
        private const val CAKE_CACHE_KEY = "cake_cache_key"
    }
}