package br.com.order.app.model

import java.time.LocalDateTime

data class Order(
    val orderId: Int,
    val date: LocalDateTime,
    val totalAmount: Double
)
