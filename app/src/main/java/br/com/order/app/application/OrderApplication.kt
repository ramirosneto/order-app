package br.com.order.app.application

import android.app.Application
import br.com.order.app.di.OrderModule
import org.koin.core.context.startKoin

class OrderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(OrderModule.instance)
        }
    }
}