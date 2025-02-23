package br.com.order.app.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.order.app.R
import br.com.order.app.model.OrderItem
import br.com.order.app.ui.view.OrderItemRow


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onAddClick: (List<OrderItem>) -> Unit,
    onDismissRequest: () -> Unit
) {
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
    var showConfirmationAlert by remember { mutableStateOf(false) }

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
                    label = { Text(stringResource(R.string.unit_amount)) },
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
                            orderId = 0,
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
                        text = stringResource(R.string.quantity),
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.unit_amount),
                        modifier = Modifier.weight(2f),
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    ),
                    enabled = orderItems.size > 0,
                    onClick = {
                        showConfirmationAlert = true
                    }
                ) {
                    Text(
                        text = stringResource(R.string.place_order),
                        fontSize = 16.sp
                    )
                }
            }
        }

        if (showConfirmationAlert) {
            ConfirmationDialog(
                title = stringResource(R.string.confirmation_dialog_title),
                message = stringResource(R.string.place_order_dialog_confirm_msg),
                onConfirm = {
                    onAddClick(orderItems.toList())
                },
                onDismiss = { showConfirmationAlert = false }
            )
        }
    }
}