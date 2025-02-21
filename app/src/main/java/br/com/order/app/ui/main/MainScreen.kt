package br.com.order.app.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.order.app.R
import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import br.com.order.app.utils.format
import br.com.order.app.utils.formatPrice
import br.com.order.app.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val orders = viewModel.orders.collectAsState()

    orders.value?.let {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(it) { order ->
                OrderRow(order)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    Button(
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(Color.Blue),
        modifier = Modifier
            .size(120.dp)
            .background(Color.Blue),
        onClick = {
            showBottomSheet = true
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                contentDescription = "Add Icon"
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Add pedido",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }

    if (showBottomSheet) {
        BottomSheet(
            onAddClick = { orderItems ->
                viewModel.addOrder(orderItems)
                showBottomSheet = false
            },
            onDismissRequest = { showBottomSheet = false }
        )
    }
}

@Composable
fun OrderRow(order: Order) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = order.orderId.toString(), modifier = Modifier.weight(1f))
        Text(text = order.date.format(), modifier = Modifier.weight(1f))
        Text(text = order.totalAmount.formatPrice(), modifier = Modifier.weight(1f))
    }
}

@Composable
fun OrderItemRow(orderItem: OrderItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = orderItem.description, modifier = Modifier.weight(1f))
        Text(text = orderItem.unitPrice.formatPrice(), modifier = Modifier.weight(1f))
        Text(text = orderItem.quantity.toString(), modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onAddClick: (List<OrderItem>) -> Unit, onDismissRequest: () -> Unit) {
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusRequester = remember { FocusRequester() }
    val orderItems = remember { mutableStateListOf<OrderItem>() }
    val description = remember { mutableStateOf("") }
    val quantity = remember { mutableStateOf("") }
    val unitPrice = remember { mutableStateOf("") }
    val totalPrice = remember { mutableStateOf("0") }
    var descriptionError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var unitPriceError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = onDismissRequest
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                LazyColumn {
                    items(orderItems) { orderItem ->
                        OrderItemRow(orderItem)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(vertical = 16.dp, horizontal = 0.dp),
                    onClick = { onAddClick(orderItems.toList()) }
                ) {
                    Text(
                        text = "Realizar pedido",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = description.value,
                onValueChange = {
                    description.value = it
                    descriptionError = false
                },
                label = { Text("Descrição") },
                isError = descriptionError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .focusRequester(focusRequester)
            )
            if (descriptionError) {
                Text(
                    text = "Campo obrigatório",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = unitPrice.value,
                    onValueChange = {
                        unitPrice.value = it
                        unitPriceError = false
                    },
                    label = { Text("Valor") },
                    isError = unitPriceError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.padding(end = 8.dp)
                )
                if (unitPriceError) {
                    Text(
                        text = "",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                OutlinedTextField(
                    value = quantity.value,
                    onValueChange = {
                        quantity.value = it
                        quantityError = false
                    },
                    label = { Text("Qtd") },
                    isError = quantityError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (quantityError) {
                    Text(
                        text = "",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(vertical = 16.dp, horizontal = 0.dp),
                onClick = {
                    val qty = quantity.value.toIntOrNull()
                    val price = unitPrice.value.toDoubleOrNull()

                    descriptionError = description.value.isEmpty()
                    quantityError = qty == null
                    unitPriceError = price == null

                    if (descriptionError.not() && quantityError.not() && unitPriceError.not()) {
                        val itemTotalPrice = quantity.value.toDouble() * unitPrice.value.toInt()
                        val orderItem = OrderItem(
                            description = description.value,
                            quantity = quantity.value.toInt(),
                            unitPrice = unitPrice.value.toDouble(),
                            totalPrice = itemTotalPrice
                        )
                        totalPrice.value =
                            (totalPrice.value.toDouble() + itemTotalPrice).toString()
                        orderItems.add(orderItem)
                        description.value = ""
                        quantity.value = ""
                        unitPrice.value = ""
                        focusRequester.requestFocus()
                    }
                }
            ) {
                Text(
                    text = "Adicionar item",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}