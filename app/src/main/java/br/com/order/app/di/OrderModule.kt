package br.com.order.app.di

import android.app.Application
import androidx.room.Room
import br.com.order.app.data.OrderDao
import br.com.order.app.data.OrderDatabase
import br.com.order.app.data.OrderRepository
import br.com.order.app.utils.OrderMapper
import br.com.order.app.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object OrderModule {

    val instance = module {
        single { provideDatabase(get()) }

        single { provideOrderDao(get()) }

        single { OrderMapper() }

        single { OrderRepository(get(), get()) }

        viewModel { MainViewModel(get()) }
    }
}

fun provideDatabase(application: Application): OrderDatabase {
    return Room.databaseBuilder(application, OrderDatabase::class.java, "orders-db")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideOrderDao(database: OrderDatabase): OrderDao {
    return database.orderDao()
}
