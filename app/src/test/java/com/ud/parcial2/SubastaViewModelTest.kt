package com.ud.parcial2

// Importaciones necesarias para pruebas unitarias con LiveData, corutinas y MockK
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

    // Regla para ejecutar LiveData al instante, necesaria para pruebas unitarias
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Dispatcher de prueba para controlar el hilo principal durante tests
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SubastaViewModel
    private lateinit var mockRepo: SubastaRepository

    // Se configura el entorno antes de cada prueba
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Se redirige el hilo principal al dispatcher de prueba
        mockRepo = mockk() // Se crea un mock del repositorio
        viewModel = SubastaViewModel(mockRepo) // Se inicializa el ViewModel con el repositorio falso
    }

    // Prueba unitaria para verificar que cargarSubastas actualiza correctamente el LiveData
    @Test
    fun `cargarSubastas should update subastas LiveData`() = runTest {
        // Arrange: se definen datos simulados y se configura el comportamiento del mock
        val subastasFalsas = listOf(
            Subasta(id = 1, titulo = "Subasta 1", descripcion = "Descripción 1", estado = "activa"),
            Subasta(id = 2, titulo = "Subasta 2", descripcion = "Descripción 2", estado = "cerrada")
        )
        coEvery { mockRepo.obtenerSubastas() } returns subastasFalsas

        // Act: se llama a la función que se quiere probar
        viewModel.cargarSubastas()
        advanceUntilIdle() // Espera a que terminen todas las corutinas

        // Assert: se verifica que el valor del LiveData coincida con los datos simulados
        assertEquals(subastasFalsas, viewModel.subastas.value)
    }

    // Limpieza posterior a cada prueba
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Se restablece el Dispatcher principal
    }
}
