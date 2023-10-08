package com.android.fastfood.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.fastfood.data.dummy.FoodMenuDataSourceImpl
import com.android.fastfood.data.local.database.AppDatabase.Companion.getInstance
import com.android.fastfood.data.local.database.dao.CartDao
import com.android.fastfood.data.local.database.dao.FoodMenuDao
import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.data.local.database.entitiy.FoodMenuEntity
import com.android.fastfood.data.local.database.mapper.toFoodMenuEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(
    entities = [CartEntity::class, FoodMenuEntity::class],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun foodMenuDao(): FoodMenuDao

    companion object {
        private const val DB_NAME = "FastFood.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(DatabaseSeederCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

class DatabaseSeederCallback(private val context: Context) : RoomDatabase.Callback() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            getInstance(context).foodMenuDao().insertFood(prepopulateMenus())
            getInstance(context).cartDao().insertCarts(prepopulateCarts())
        }
    }

    private fun prepopulateMenus(): List<FoodMenuEntity> {
        return FoodMenuDataSourceImpl().getFood().toFoodMenuEntity()
    }

    private fun prepopulateCarts(): List<CartEntity> {
        return mutableListOf(
            CartEntity(
                id = 1,
                foodId = 1,
                orderNotes = "",
                itemQuantity = 3
            ),
            CartEntity(
                id = 2,
                foodId = 2,
                orderNotes = "",
                itemQuantity = 6
            ),
        )
    }
}