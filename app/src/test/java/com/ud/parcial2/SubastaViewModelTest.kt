package com.ud.parcial2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ud.parcial2.model.Subasta
import com.ud.parcial2.repository.SubastaRepository
import com.ud.parcial2.viewmodel.SubastaViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubastaViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SubastaViewModel
    private lateinit var mockRepo: SubastaRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepo = mockk()
        viewModel = SubastaViewModel(mockRepo)
    }

    @Test
    fun `cargarSubastas should update subastas LiveData`() = runTest {
        // Arrange
        val subastasFalsas = listOf(
            Subasta(id = 1, titulo = "Subasta 1", descripcion = "Descripción 1", estado = "activa"),
            Subasta(id = 2, titulo = "Subasta 2", descripcion = "Descripción 2", estado = "cerrada")
        )
        coEvery { mockRepo.obtenerSubastas() } returns subastasFalsas

        // Act
        viewModel.cargarSubastas()
        advanceUntilIdle() // Espera a que corutinas terminen

        // Assert
        assertEquals(subastasFalsas, viewModel.subastas.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
