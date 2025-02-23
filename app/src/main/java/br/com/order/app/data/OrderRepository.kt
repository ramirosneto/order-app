package br.com.order.app.data

import androidx.room.Transaction
import br.com.order.app.model.OrderItem
import br.com.order.app.model.OrderWithItems
import br.com.order.app.utils.OrderMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import kotlin.math.round

class OrderRepository(
    private val mapper: OrderMapper,
    private val orderDao: OrderDao
) {

    @Transaction
    suspend fun insertOrder(orderItems: List<OrderItem>) {
        val totalAmount = orderItems.sumOf { it.totalPrice }
        val now = LocalDateTime.now().toString()
        val orderEntity = OrderEntity(date = now, totalAmount = round(totalAmount))
        val orderId = orderDao.insertOrder(orderEntity)

        val orderItemEntities = orderItems.map { orderItem ->
            mapper.mapOrderItemModelToEntity(orderId, orderItem)
        }

        orderDao.insertOrderItems(orderItemEntities)
    }

    @Transaction
    fun getAllOrders(): Flow<List<OrderWithItems>> {
        val orders = orderDao.getAllOrders().map { ordersEntity ->
            ordersEntity.map { mapper.mapOrderEntityToModel(it) }
        }
        val ordersItems = orderDao.getAllOrderItems().map { orderItemEntity ->
            orderItemEntity.map { mapper.mapOrderItemEntityToModel(it) }
        }

        return orders.combine(ordersItems) { ordersList, itemsList ->
            ordersList.map { order ->
                val items = itemsList.filter { it.orderId == order.orderId }
                OrderWithItems(order, items)
            }
        }
    }

    suspend fun deleteOrder(orderId: Long) {
        orderDao.deleteOrderAndItems(orderId)
    }
}
