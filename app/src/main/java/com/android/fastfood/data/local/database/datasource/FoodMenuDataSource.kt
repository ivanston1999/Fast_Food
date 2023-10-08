package com.android.fastfood.data.local.database.datasource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.fastfood.data.local.database.dao.FoodMenuDao
import com.android.fastfood.data.local.database.entitiy.FoodMenuEntity
import kotlinx.coroutines.flow.Flow

interface FoodMenuDataSource {
    fun getAllFoodMenu(): Flow<List<FoodMenuEntity>>
    fun getFoodById(id: Int): Flow<FoodMenuEntity>
    suspend fun insertFood(food: List<FoodMenuEntity>)
    suspend fun deleteFood(food: FoodMenuEntity): Int
    suspend fun updateFood(food: FoodMenuEntity): Int
}

class FoodMenuDatabaseDataSource(private val FoodMenuDao: FoodMenuDao):FoodMenuDataSource{
    override fun getAllFoodMenu(): Flow<List<FoodMenuEntity>> {
        return FoodMenuDao.getAllFoodMenu()
    }

    override fun getFoodById(id: Int): Flow<FoodMenuEntity> {
        return FoodMenuDao.getFoodById(id)
    }

    override suspend fun insertFood(food: List<FoodMenuEntity>) {
        return FoodMenuDao.insertFood(food)
    }

    override suspend fun deleteFood(food: FoodMenuEntity): Int {
        return FoodMenuDao.deleteFood(food)
    }

    override suspend fun updateFood(food: FoodMenuEntity): Int {
        return FoodMenuDao.updateFood(food)
    }

}