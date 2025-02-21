package br.com.order.app.application

import android.app.Application
import br.com.order.app.di.OrderModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class OrderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@OrderApplication)
            modules(OrderModule.instance)
        }
    }
}