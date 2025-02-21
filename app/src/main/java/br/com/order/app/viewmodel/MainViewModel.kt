package br.com.order.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.order.app.data.OrderRepository
import br.com.order.app.model.OrderItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: OrderRepository) : ViewModel() {

    //private val _orders: MutableState<List<Order>> = mutableStateListOf<Order>()

    fun addOrder(orderItems: List<OrderItem>) {
        viewModelScope.launch {
            repository.insertOrder(orderItems)
        }
    }
}
