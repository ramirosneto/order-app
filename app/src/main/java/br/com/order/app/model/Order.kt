package br.com.order.app.model

data class Order(
    val orderId: Int,
    val date: String,
    val totalAmount: Double
)
