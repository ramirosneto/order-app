package br.com.order.app.model

data class OrderItem(
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)
