package com.flicker.cvs.feature.flimage

import app.cash.turbine.test
import com.flicker.cvs.feature.flimage.domain.FLImageUseCase
import com.flicker.cvs.feature.flimage.presentation.FlickerViewModel
import com.flicker.cvs.feature.flimage.presentation.StateError
import com.flicker.cvs.feature.flimage.presentation.StateLoading
import com.flicker.cvs.feature.flimage.presentation.StateSuccess
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class FlickerViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchFlickerData emits Loading and them StateSuccess when response is successful`() = runTest {
        // Given
        val tag = "testTag"
        val viewModel = FlickerViewModel(UnconfinedTestDispatcher(), UnconfinedTestDispatcher())

        // When
        viewModel.fetchFlickerData(tag)

        // Then
        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(actual = initial, expected = StateLoading)
            val success = awaitItem()
            assertTrue { success is StateSuccess }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchFlickerData emits Loading and them StateError when response is an Error`() = runTest {
        // Given
        val tag = "testTag"
        val mockUseCase = mockk<FLImageUseCase>()
        coEvery { mockUseCase.getImages(any()) } returns Response.error(500, mockk(relaxed = true))
        val viewModel = FlickerViewModel(UnconfinedTestDispatcher(), UnconfinedTestDispatcher(), mockUseCase)

        // When
        viewModel.fetchFlickerData(tag)

        // Then
        viewModel.uiState.test {
            val error = awaitItem()
            assertTrue { error is StateError }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchFlickerData always start with loading state`() = runTest {
        // Given
        val tag = "testTag"
        val viewModel = FlickerViewModel(UnconfinedTestDispatcher(), UnconfinedTestDispatcher())

        // When
        viewModel.fetchFlickerData(tag)

        // Then
        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(actual = initial, expected = StateLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}