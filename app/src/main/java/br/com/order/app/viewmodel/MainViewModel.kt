package br.com.order.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.order.app.data.OrderRepository
import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: OrderRepository) : ViewModel() {

    val orders: StateFlow<List<Order>> =
        repository.getAllOrders().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addOrder(orderItems: List<OrderItem>) {
        viewModelScope.launch {
            repository.insertOrder(orderItems)
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            repository.deleteOrder(order.orderId)
        }
    }
}
