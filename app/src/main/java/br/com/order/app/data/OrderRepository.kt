package br.com.order.app.data

class OrderRepository constructor(private val orderDao: OrderDao) {
    suspend fun insertOrder(order: Order): Long {
        return orderDao.insertOrder(order)
    }

    suspend fun insertOrderItem(orderItem: OrderItem) {
        orderDao.insertOrderItem(orderItem)
    }

    suspend fun getAllOrders(): List<Order> {
        return orderDao.getAllOrders()
    }
}