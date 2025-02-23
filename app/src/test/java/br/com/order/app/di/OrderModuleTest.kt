package br.com.order.app.di

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.order.app.data.OrderDao
import br.com.order.app.data.OrderDatabase
import br.com.order.app.data.OrderRepository
import br.com.order.app.utils.OrderMapper
import br.com.order.app.viewmodel.MainViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.mock

class OrderModuleTest : KoinTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testModule = module {
        single { provideDatabase(mock(Application::class.java)) }
        single { provideOrderDao(get()) }
        single { OrderMapper() }
        single { OrderRepository(get(), get()) }
        viewModel { MainViewModel(get()) }
    }

    private val orderRepository: OrderRepository by inject()
    private val mainViewModel: MainViewModel by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(testModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test provideDatabase`() {
        val application = mock(Application::class.java)
        val database = provideDatabase(application)
        assert(database is OrderDatabase)
    }

    @Test
    fun `test provideOrderDao`() {
        val application = mock(Application::class.java)
        val database = provideDatabase(application)
        val orderDao = provideOrderDao(database)
        assert(orderDao is OrderDao)
    }

    @Test
    fun `test OrderRepository injection`() {
        assert(orderRepository is OrderRepository)
    }

    @Test
    fun `test MainViewModel injection`() {
        assert(mainViewModel is MainViewModel)
    }
}