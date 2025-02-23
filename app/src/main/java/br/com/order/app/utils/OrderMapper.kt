package br.com.order.app.utils

import br.com.order.app.data.OrderEntity
import br.com.order.app.data.OrderItemEntity
import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem

class OrderMapper {

    fun mapOrderModelToEntity(orderModel: Order): OrderEntity {
        return OrderEntity(
            orderId = orderModel.orderId,
            date = orderModel.date.toString(),
            totalAmount = orderModel.totalAmount
        )
    }

    fun mapOrderEntityToModel(orderEntity: OrderEntity): Order {
        return Order(
            orderId = orderEntity.orderId,
            date = orderEntity.date.format(),
            totalAmount = orderEntity.totalAmount
        )
    }

    fun mapOrderItemModelToEntity(orderId: Long, orderItemModel: OrderItem): OrderItemEntity {
        return OrderItemEntity(
            orderId = orderId,
            description = orderItemModel.description,
            quantity = orderItemModel.quantity,
            unitPrice = orderItemModel.unitPrice,
            totalPrice = orderItemModel.totalPrice
        )
    }

    fun mapOrderItemEntityToModel(orderItemEntity: OrderItemEntity): OrderItem {
        return OrderItem(
            orderId = orderItemEntity.orderId,
            description = orderItemEntity.description,
            quantity = orderItemEntity.quantity,
            unitPrice = orderItemEntity.unitPrice,
            totalPrice = orderItemEntity.totalPrice
        )
    }
}