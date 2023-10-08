package com.android.fastfood.data.local.database.datasource

import com.android.fastfood.data.local.database.dao.CartDao
import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.data.local.database.relation.FoodMenuCartRelation
import kotlinx.coroutines.flow.Flow

interface CartDataSource {

    fun getAllCarts(): Flow<List<FoodMenuCartRelation>>
    fun getCartById(cartId: Int): Flow<FoodMenuCartRelation>
    suspend fun insertCart(cart: CartEntity): Long
    suspend fun insertCarts(cart: List<CartEntity>)
    suspend fun deleteCart(cart: CartEntity): Int
    suspend fun updateCart(cart: CartEntity): Int

}

class CartDatabaseDataSource(private val CartDao: CartDao): CartDataSource {
    override fun getAllCarts(): Flow<List<FoodMenuCartRelation>> {
        return CartDao.getAllCarts()
    }

    override fun getCartById(cartId: Int): Flow<FoodMenuCartRelation> {
        return CartDao.getCartById(cartId)
    }

    override suspend fun insertCart(cart: CartEntity): Long {
        return CartDao.insertCart(cart)
    }

    override suspend fun insertCarts(cart: List<CartEntity>) {
        return CartDao.insertCarts(cart)
    }

    override suspend fun deleteCart(cart: CartEntity): Int {
        return CartDao.deleteCart(cart)
    }

    override suspend fun updateCart(cart: CartEntity): Int {
        return CartDao.updateCart(cart)
    }

}