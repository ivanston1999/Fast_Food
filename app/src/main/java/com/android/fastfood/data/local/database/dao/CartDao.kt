package com.android.fastfood.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.data.local.database.relation.FoodMenuCartRelation
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {
    @Query("SELECT * FROM CART")
    fun getAllCarts(): Flow<List<FoodMenuCartRelation>>

    @Query("SELECT * FROM CART WHERE id == :cartId")
    fun getCartById(cartId: Int): Flow<FoodMenuCartRelation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarts(cart: List<CartEntity>)

    @Delete
    suspend fun deleteCart(cart: CartEntity): Int

    @Update
    suspend fun updateCart(cart: CartEntity): Int
}