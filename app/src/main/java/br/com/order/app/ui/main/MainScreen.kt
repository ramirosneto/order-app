package br.com.order.app.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.order.app.R
import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import br.com.order.app.ui.components.ActionIcon
import br.com.order.app.ui.components.SwipeableItemWithActions
import br.com.order.app.utils.formatDate
import br.com.order.app.utils.formatPrice
import br.com.order.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val orders = viewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pedidos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = Color.Blue
            ) {
                Icon(
                    Icons.Default.Add,
                    tint = Color.White,
                    contentDescription = "Adicionar Pedido"
                )
            }
        },
        content = {
            orders.value?.let {
                Column(modifier = Modifier.padding(top = 32.dp)) {
                    Row {
                        Text(
                            text = stringResource(R.string.description),
                            modifier = Modifier.weight(3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.amount),
                            modifier = Modifier.weight(2f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.quantity),
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "", modifier = Modifier.weight(1f))
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(it) { order ->
                            Card(
                                elevation = CardDefaults.cardElevation(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                SwipeableItemWithActions(
                                    isRevealed = order.isDeleteRevealed,
                                    onExpanded = { order.isDeleteRevealed = true },
                                    onCollapsed = { order.isDeleteRevealed = false },
                                    actions = {
                                        ActionIcon(
                                            onClick = {
                                                viewModel.deleteOrder(order)
                                            },
                                            backgroundColor = Color.Red,
                                            icon = Icons.Default.Delete,
                                            modifier = Modifier.fillMaxHeight()
                                        )
                                    },
                                ) {
                                    OrderRow(order)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    )

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
            .padding(16.dp)
    ) {
        Text(text = order.orderId.toString(), modifier = Modifier.weight(1f))
        Text(text = order.date.formatDate(), modifier = Modifier.weight(3f))
        Text(text = order.totalAmount.formatPrice(), modifier = Modifier.weight(2f))
    }
}

@Composable
fun OrderItemRow(orderItem: OrderItem, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = orderItem.description,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(3f)
        )
        Text(
            text = orderItem.unitPrice.formatPrice(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(2f)
        )
        Text(
            text = orderItem.quantity.toString(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        IconButton(
            onClick = onRemoveClick
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .weight(1f),
                painter = rememberVectorPainter(image = Icons.Filled.Delete),
                contentDescription = "Trash Can Icon"
            )
        }
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
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = description.value,
                onValueChange = {
                    description.value = it
                    descriptionError = false
                },
                label = { Text(stringResource(R.string.description)) },
                isError = descriptionError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .focusRequester(focusRequester)
            )
            if (descriptionError) {
                Text(
                    text = stringResource(R.string.mandatory_field),
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
                    label = { Text(stringResource(R.string.amount)) },
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
                    label = { Text(stringResource(R.string.quantity)) },
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
                        val itemTotalPrice = quantity.value.toInt() * unitPrice.value.toDouble()
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
                    text = stringResource(R.string.add_order_item),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Text(
                        text = stringResource(R.string.description),
                        modifier = Modifier.weight(3f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.amount),
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.quantity),
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "", modifier = Modifier.weight(1f))
                }

                LazyColumn {
                    items(orderItems) { orderItem ->
                        OrderItemRow(orderItem) {
                            orderItems.remove(orderItem)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(vertical = 16.dp, horizontal = 0.dp),
                    enabled = orderItems.size > 0,
                    onClick = { onAddClick(orderItems.toList()) }
                ) {
                    Text(
                        text = stringResource(R.string.place_order),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}