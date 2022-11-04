package com.itechevo.domain

import com.itechevo.domain.model.Cake
import com.itechevo.domain.model.NetworkResult
import com.itechevo.domain.repository.CakeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CakeInteractorTest {

    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    //region mocks
    private val mockCakeRepository = mockk<CakeRepository>(relaxed = true)
    //endregion

    private val sut = CakeInteractor(mockCakeRepository, dispatcher)

    private val apiResult = NetworkResult.Success(
        listOf(
            Cake("C 3", "Desc 3", "URL 3"),
            Cake("A 1", "Desc 1", "URL 1"),
            Cake("D 4", "Desc 4", "URL 4"),
            Cake("B 2", "Desc 2", "URL 2"),
            Cake("A 1", "Desc 1", "URL 1"),
        )
    )

    private val expectedResult = NetworkResult.Success(
        listOf(
            Cake("A 1", "Desc 1", "URL 1"),
            Cake("B 2", "Desc 2", "URL 2"),
            Cake("C 3", "Desc 3", "URL 3"),
            Cake("D 4", "Desc 4", "URL 4")
        )
    )

    @Test
    fun `given result when getOrderedCakes then return ordered cakes with no duplicates`() =
        runTest {

            //given
            coEvery { mockCakeRepository.getCakes() } returns flowOf(apiResult)

            //when
            val result = sut.getOrderedCakes().toList()

            //then
            coVerify { mockCakeRepository.getCakes() }
            assertEquals(1, result.size)
            val cakes = result.first() as NetworkResult.Success<List<Cake>>
            //Removed duplicate check
            assertEquals(4, cakes.data.size)
            //Ordered check
            assertEquals(expectedResult.data, cakes.data)
        }

    @Test
    fun `given error when getOrderedCakes then return error`() =
        runTest {

            //given
            coEvery { mockCakeRepository.getCakes() } returns flowOf(NetworkResult.Error(Throwable()))

            //when
            val result = sut.getOrderedCakes().toList()

            //then
            coVerify { mockCakeRepository.getCakes() }
            assertEquals(1, result.size)
            assertTrue(result.first() is NetworkResult.Error)
        }
}