package br.com.order.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.order.app.data.OrderRepository
import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import br.com.order.app.model.OrderWithItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: OrderRepository) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderWithItems>>(emptyList())
    val orders: StateFlow<List<OrderWithItems>> get() = _orders

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> get() = _error

    init {
        fetchOrders()
    }

    fun fetchOrders() {
        viewModelScope.launch {
            try {
                repository.getAllOrders().collect { orders ->
                    _orders.value = orders
                }
            } catch (e: Exception) {
                _error.value = "Erro ao buscar pedidos - cod: ${e.message.toString()}"
            }
        }
    }

    fun addOrder(orderItems: List<OrderItem>) {
        viewModelScope.launch {
            try {
                repository.insertOrder(orderItems)
            } catch (e: Exception) {
                _error.value = "Erro ao adicionar pedido - cod: ${e.message.toString()}"
            }
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            try {
                repository.deleteOrder(order.orderId)
            } catch (e: Exception) {
                _error.value = "Erro ao deletar pedido - cod: ${e.message.toString()}"
            }
        }
    }
}
