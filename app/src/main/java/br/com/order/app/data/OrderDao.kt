package br.com.order.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(orderEntity: OrderEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItemEntity: OrderItemEntity)

    @Transaction
    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<OrderEntity>

    @Transaction
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getAllOrderItems(orderId: Int): List<OrderItemEntity>
}
