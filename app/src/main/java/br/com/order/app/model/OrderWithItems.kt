package br.com.order.app.model

data class OrderWithItems(
    val order: Order,
    val items: List<OrderItem>,
    var isDeleteRevealed: Boolean = false
)
