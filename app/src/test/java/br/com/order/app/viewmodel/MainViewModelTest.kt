package br.com.order.app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.order.app.data.OrderRepository
import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import br.com.order.app.model.OrderWithItems
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: OrderRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchOrders should update orders state flow with orders from repository`() = runTest {
        val mockOrders = listOf(OrderWithItems(Order(1, "2025-10-02", 2500.0), emptyList()))
        coEvery { repository.getAllOrders() } returns flow { emit(mockOrders) }

        viewModel.fetchOrders()

        assertEquals(mockOrders, viewModel.orders.value)
    }

    @Test
    fun `fetchOrders should update error state flow when exception is thrown`() = runTest {
        val errorMessage = "Erro ao buscar pedidos"
        coEvery { repository.getAllOrders() } throws Exception(errorMessage)

        viewModel.orders

        assertTrue(viewModel.error.value.contains(errorMessage))
    }

    @Test
    fun `addOrder should call repository insertOrder`() = runTest {
        val orderItems = listOf(OrderItem(1, "Item 1", 1, 150.0, 150.0))

        coEvery { repository.insertOrder(orderItems) } just Runs

        viewModel.addOrder(orderItems)

        coVerify { repository.insertOrder(orderItems) }
    }

    @Test
    fun `addOrder should update error state flow when exception is thrown`() = runTest {
        val orderItems = listOf(OrderItem(1, "Item 1", 1, 150.0, 150.0))
        val errorMessage = "Test exception"
        coEvery { repository.insertOrder(orderItems) } throws Exception(errorMessage)

        viewModel.addOrder(orderItems)

        assertTrue(viewModel.error.value.contains(errorMessage))
    }

    @Test
    fun `deleteOrder should call repository deleteOrder`() = runTest {
        val order = Order(1, "Order 1", 1500.0)

        coEvery { repository.deleteOrder(order.orderId) } just Runs

        viewModel.deleteOrder(order)

        coVerify { repository.deleteOrder(order.orderId) }
    }

    @Test
    fun `deleteOrder should update error state flow when exception is thrown`() = runTest {
        val order = Order(1, "Order 1", 1500.0)
        val errorMessage = "Test exception"
        coEvery { repository.deleteOrder(order.orderId) } throws Exception(errorMessage)

        viewModel.deleteOrder(order)

        assertTrue(viewModel.error.value.contains(errorMessage))
    }
}