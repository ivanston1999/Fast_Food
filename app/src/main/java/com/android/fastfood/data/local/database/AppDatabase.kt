package com.android.fastfood.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.fastfood.data.dummy.FoodMenuDataSourceImpl
import com.android.fastfood.data.local.database.AppDatabase.Companion.getInstance
import com.android.fastfood.data.local.database.dao.CartDao
import com.android.fastfood.data.local.database.entitiy.CartEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(
    entities = [CartEntity::class],
    version = 3,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        private const val DB_NAME = "FastFood.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

        }
    }
}

