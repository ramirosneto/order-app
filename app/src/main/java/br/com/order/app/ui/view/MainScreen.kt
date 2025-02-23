package br.com.order.app.ui.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.order.app.R
import br.com.order.app.model.Order
import br.com.order.app.model.OrderItem
import br.com.order.app.model.OrderWithItems
import br.com.order.app.ui.components.ActionIcon
import br.com.order.app.ui.components.BottomSheet
import br.com.order.app.ui.components.ConfirmationDialog
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
                title = { Text(text = stringResource(R.string.orders)) }
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
            orders.value.let {
                Column(modifier = Modifier.padding(top = 32.dp)) {
                    OrdersHeader()
                    OrdersContent(it, viewModel)
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
fun OrdersHeader() {
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
}

@Composable
fun OrdersContent(orders: List<OrderWithItems>, viewModel: MainViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(orders) { order ->
            var expanded by remember { mutableStateOf(false) }
            val density = LocalDensity.current

            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = expanded.not()
                    }
            ) {
                Column {
                    SwipeableItemWithActions(
                        isRevealed = order.isDeleteRevealed,
                        onExpanded = { order.isDeleteRevealed = true },
                        onCollapsed = { order.isDeleteRevealed = false },
                        actions = {
                            ActionIcon(
                                onClick = { showDialog = true },
                                backgroundColor = Color.Red,
                                icon = Icons.Default.Delete,
                                modifier = Modifier.fillMaxHeight()
                            )

                            if (showDialog) {
                                ConfirmationDialog(
                                    title = stringResource(R.string.confirmation_dialog_title),
                                    message = stringResource(R.string.confirmation_dialog_msg),
                                    onConfirm = { viewModel.deleteOrder(order.order) },
                                    onDismiss = { showDialog = false }
                                )
                            }
                        },
                    ) {
                        OrderRow(order.order, expanded)
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter = slideInVertically {
                            with(density) { -40.dp.roundToPx() }
                        } + expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            OrderItemHeader()
                            order.items.forEach { orderItem ->
                                OrderItemRow(orderItem, null)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun OrderRow(order: Order, isExpanded: Boolean) {
    val icon = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = order.orderId.toString(), modifier = Modifier.weight(1f))
        Text(text = order.date.formatDate(), modifier = Modifier.weight(3f))
        Text(text = order.totalAmount.formatPrice(), modifier = Modifier.weight(2f))
        Icon(
            modifier = Modifier
                .size(30.dp)
                .weight(0.5f),
            painter = rememberVectorPainter(image = icon),
            contentDescription = stringResource(R.string.delete_icon_description)
        )
    }
}

@Composable
fun OrderItemHeader() {
    Row {
        Text(
            text = stringResource(R.string.description),
            modifier = Modifier.weight(3f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.quantity),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.amount),
            modifier = Modifier.weight(2f),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OrderItemRow(orderItem: OrderItem, onRemoveClick: (() -> Unit)? = null) {
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
            text = orderItem.quantity.toString(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Text(
            text = orderItem.unitPrice.formatPrice(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(2f)
        )
        onRemoveClick?.let {
            IconButton(
                onClick = it
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f),
                    painter = rememberVectorPainter(image = Icons.Filled.Delete),
                    contentDescription = stringResource(R.string.delete_icon_description)
                )
            }
        }
    }
}

