package com.itechevo.data

import com.itechevo.data.model.CakeResponse
import com.itechevo.data.source.WaracleApiService
import com.itechevo.domain.model.Cake
import com.itechevo.domain.model.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CakeRepositoryImplTest {

    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    //region mocks
    private val mockWaracleApiService = mockk<WaracleApiService>(relaxed = true)
    //endregion

    private val sut = CakeRepositoryImpl(mockWaracleApiService, dispatcher)

    private val apiResponse =
        listOf(
            CakeResponse("A 1", "Desc 1", "URL 1"),
            CakeResponse("B 2", "Desc 2", "URL 2"),
            CakeResponse("C 3", "Desc 3", "URL 3"),
            CakeResponse("D 4", "Desc 4", "URL 4")
        )

    @Test
    fun `given success when getCakes then return cakes response`() =
        runTest {

            //given
            coEvery { mockWaracleApiService.getCakes() } returns apiResponse

            //when
            val result = sut.getCakes().toList()

            //then
            coVerify { mockWaracleApiService.getCakes(any()) }

            assertEquals(1, result.size)
            val cakes = result.first() as NetworkResult.Success<List<Cake>>
            assertEquals(apiResponse.size, cakes.data.size)
        }

    @Test
    fun `given error when getCakes then return error`() =
        runTest {

            //given
            coEvery { mockWaracleApiService.getCakes() } throws Throwable()

            //when
            val result = sut.getCakes().toList()

            //then
            coVerify { mockWaracleApiService.getCakes(any()) }

            assertEquals(1, result.size)
            assertTrue(result.first() is NetworkResult.Error)
        }

    @Test
    fun `given cached cakes when getCakes then return cached response before API response`() =
        runTest {

            val cacheResponse = listOf(
                CakeResponse("Title 1", "Desc 1", "URL 1"),
                CakeResponse("Title 2", "Desc 2", "URL 2")
            )

            //given
            coEvery { mockWaracleApiService.getCakes() } returns cacheResponse
            sut.getCakes().toList()

            //given cached
            coEvery { mockWaracleApiService.getCakes() } returns apiResponse

            //when
            val result = sut.getCakes().toList()

            //then
            assertEquals(2, result.size)

            //cached result
            val cachedResult = result.first() as NetworkResult.Success<List<Cake>>
            assertEquals(cacheResponse.size, cachedResult.data.size)

            //api result
            val apiResult = result[1] as NetworkResult.Success<List<Cake>>
            assertEquals(apiResponse.size, apiResult.data.size)
        }
}