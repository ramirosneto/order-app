package br.com.order.app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OrderEntity::class, OrderItemEntity::class], version = 1)
abstract class OrderDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
}
