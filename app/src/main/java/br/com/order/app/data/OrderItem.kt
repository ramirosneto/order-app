package br.com.order.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val itemId: Long = 0,
    val orderId: Long,
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)