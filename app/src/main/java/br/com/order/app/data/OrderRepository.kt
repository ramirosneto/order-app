package br.com.order.app.data

import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import br.com.order.app.utils.OrderMapper
import java.time.LocalDateTime

class OrderRepository(
    private val mapper: OrderMapper,
    private val orderDao: OrderDao
) {

    suspend fun insertOrder(orderItems: List<OrderItem>) {
        val totalAmount = orderItems.sumOf { it.totalPrice }
        val orderEntity = OrderEntity(date = LocalDateTime.now(), totalAmount = totalAmount)
        val orderId = orderDao.insertOrder(orderEntity)
        orderItems.forEach { orderItem ->
            orderDao.insertOrderItem(mapper.mapOrderItemModelToEntity(orderId, orderItem))
        }
    }

    suspend fun getAllOrders(): List<Order> {
        val orders = arrayListOf<Order>()
        orderDao.getAllOrders().forEach { order ->
            orders.add(mapper.mapOrderEntityToModel(order))
        }

        return orders
    }

    suspend fun getAllOrderItems(orderId: Int): List<OrderItem> {
        val orderItems = arrayListOf<OrderItem>()
        orderDao.getAllOrderItems(orderId).forEach { orderItem ->
            orderItems.add(mapper.mapOrderItemEntityToModel(orderItem))
        }

        return orderItems
    }
}
