package br.com.order.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.order.app.data.OrderRepository
import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: OrderRepository) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>?>(null)
    val orders: StateFlow<List<Order>?> get() = _orders

    init {
        getOrders()
    }

    private fun getOrders() {
        viewModelScope.launch {
            _orders.value = repository.getAllOrders()
        }
    }

    fun addOrder(orderItems: List<OrderItem>) {
        viewModelScope.launch {
            repository.insertOrder(orderItems)
        }
    }
}
