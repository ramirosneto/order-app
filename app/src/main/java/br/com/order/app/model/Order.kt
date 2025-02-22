package br.com.order.app.model

data class Order(
    val orderId: Long,
    val date: String,
    val totalAmount: Double,
    var isDeleteRevealed: Boolean = false
)
