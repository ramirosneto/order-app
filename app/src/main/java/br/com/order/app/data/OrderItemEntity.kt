package br.com.order.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val itemId: Int = 0,
    val orderId: Int,
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)