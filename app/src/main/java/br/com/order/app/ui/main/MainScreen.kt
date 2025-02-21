package br.com.order.app.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.order.app.R
import br.com.order.app.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    var showBottomSheet by remember { mutableStateOf(false) }

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
            onAddClick = {},
            onDismissRequest = { showBottomSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onAddClick: () -> Unit, onDismissRequest: () -> Unit) {
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val description = remember { mutableStateOf("") }
    val quantity = remember { mutableStateOf("") }
    val unitPrice = remember { mutableStateOf("") }
    val totalPrice = remember { mutableStateOf("") }

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Descrição") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = unitPrice.value,
                    onValueChange = { unitPrice.value = it },
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = quantity.value,
                    onValueChange = { quantity.value = it },
                    label = { Text("Qtd") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(vertical = 16.dp, horizontal = 0.dp),
                onClick = onAddClick
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
