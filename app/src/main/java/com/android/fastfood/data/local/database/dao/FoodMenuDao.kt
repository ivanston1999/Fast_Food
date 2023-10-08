package com.android.fastfood.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.fastfood.data.local.database.entitiy.FoodMenuEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FoodMenuDao {

    @Query("SELECT * FROM FOODMENU")
    fun getAllFoodMenu(): Flow<List<FoodMenuEntity>>

    @Query("SELECT * FROM FOODMENU WHERE id == :id")
    fun getFoodById(id: Int): Flow<FoodMenuEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: List<FoodMenuEntity>)

    @Delete
    suspend fun deleteFood(food: FoodMenuEntity): Int

    @Update
    suspend fun updateFood(food: FoodMenuEntity): Int

}