package com.itechevo.waraclecakes.ui

import com.itechevo.domain.model.Cake
import com.itechevo.domain.model.NetworkResult
import com.itechevo.domain.usecase.CakeUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CakeViewModelTest : BaseViewModelTest() {

    //region mocks
    private val mockCakeUseCase = mockk<CakeUseCase>()
    //endregion

    private val emittedUiStates = mutableListOf<CakeViewModel.UiState>()

    private lateinit var sut: CakeViewModel

    private val expectedResults = listOf(
        Cake("A 1", "Desc 1", "URL 1"),
        Cake("B 2", "Desc 2", "URL 2"),
        Cake("C 3", "Desc 3", "URL 3"),
        Cake("D 4", "Desc 4", "URL 4")
    )

    @Test
    fun `given cakes result when init then emit content`() = runTest {

        //given
        coEvery { mockCakeUseCase.getOrderedCakes() } returns flow {
            emit(NetworkResult.Success(expectedResults))
        }

        //when
        sut = CakeViewModel(mockCakeUseCase)

        val collectJob = launch(testDispatcher) {
            sut.uiState.toList(emittedUiStates)
        }

        //then
        coVerify { mockCakeUseCase.getOrderedCakes() }
        assertEquals(1, emittedUiStates.size)
        assertTrue(emittedUiStates.first() is CakeViewModel.UiState.Content)
        val content = emittedUiStates.first() as CakeViewModel.UiState.Content
        assertEquals(expectedResults, content.result)

        collectJob.cancel()
    }

    @Test
    fun `given error when init then emit error`() = runTest {

        //given
        coEvery { mockCakeUseCase.getOrderedCakes() } returns flow {
            emit(NetworkResult.Error(Throwable()))
        }

        //when
        sut = CakeViewModel(mockCakeUseCase)

        val collectJob = launch(testDispatcher) {
            sut.uiState.toList(emittedUiStates)
        }

        //then
        coVerify { mockCakeUseCase.getOrderedCakes() }
        assertEquals(1, emittedUiStates.size)
        assertTrue(emittedUiStates.first() is CakeViewModel.UiState.Error)

        collectJob.cancel()
    }
}