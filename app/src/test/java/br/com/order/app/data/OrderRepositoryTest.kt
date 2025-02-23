package br.com.order.app.data

import br.com.order.app.model.OrderItem
import br.com.order.app.utils.OrderMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OrderRepositoryTest {

    private lateinit var orderDao: OrderDao
    private lateinit var mapper: OrderMapper
    private lateinit var repository: OrderRepository

    @Before
    fun setUp() {
        orderDao = mockk()
        mapper = mockk()
        repository = OrderRepository(mapper, orderDao)
    }

    @Test
    fun `test insertOrder`() = runTest {
        val orderItems = listOf(OrderItem(1, "Item 1", 1, 1.0, 1.0))
        val orderId = 1L

        coEvery { orderDao.insertOrder(any()) } returns orderId
        coEvery { orderDao.insertOrderItems(any()) } returns Unit
        coEvery { mapper.mapOrderItemModelToEntity(any(), any()) } returns OrderItemEntity(
            1,
            orderId,
            "Item 1",
            1,
            1.0,
            1.0
        )

        repository.insertOrder(orderItems)

        coVerify { orderDao.insertOrder(any()) }
        coVerify { orderDao.insertOrderItems(any()) }
    }

    @Test
    fun `test deleteOrder`() = runTest {
        val orderId = 1L

        coEvery { orderDao.deleteOrderAndItems(orderId) } returns Unit

        repository.deleteOrder(orderId)

        coVerify { orderDao.deleteOrderAndItems(orderId) }
    }
}