package br.com.order.app.data

import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import br.com.order.app.utils.OrderMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class OrderRepository(
    private val mapper: OrderMapper,
    private val orderDao: OrderDao
) {

    suspend fun insertOrder(orderItems: List<OrderItem>) {
        val totalAmount = orderItems.sumOf { it.totalPrice }
        val now = LocalDateTime.now().toString()
        val orderEntity = OrderEntity(date = now, totalAmount = totalAmount)
        val orderId = orderDao.insertOrder(orderEntity)
        orderItems.forEach { orderItem ->
            orderDao.insertOrderItem(mapper.mapOrderItemModelToEntity(orderId, orderItem))
        }
    }

    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { ordersEntity ->
            ordersEntity.map { mapper.mapOrderEntityToModel(it) }
        }
    }

    fun getAllOrderItems(orderId: Long): Flow<List<OrderItem>> {
        return orderDao.getAllOrderItems(orderId).map { orderItemEntity ->
            orderItemEntity.map { mapper.mapOrderItemEntityToModel(it) }
        }
    }

    suspend fun deleteOrder(orderId: Long) {
        orderDao.deleteOrder(orderId)
    }
}
