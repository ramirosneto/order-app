package br.com.order.app.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import br.com.order.app.ui.main.MainScreen
import br.com.order.app.ui.theme.OrderAppTheme
import br.com.order.app.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderAppTheme {
                MainScreen(viewModel)
            }
        }
    }
}
