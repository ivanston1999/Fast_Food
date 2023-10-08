package com.android.fastfood.data.repository

import android.view.Menu
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.fastfood.data.local.database.datasource.CartDataSource
import com.android.fastfood.data.local.database.datasource.FoodMenuDataSource
import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.data.local.database.mapper.toCartEntity
import com.android.fastfood.data.local.database.mapper.toCartFoodMenuList
import com.android.fastfood.data.local.database.relation.FoodMenuCartRelation
import com.android.fastfood.model.Cart
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.model.FoodMenuCart
import com.android.fastfood.utils.ResultWrapper
import com.android.fastfood.utils.proceed
import com.android.fastfood.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.lang.IllegalStateException

interface CartRepository {

    fun getCartData(): Flow<ResultWrapper<Pair<List<FoodMenuCart>, Double>>>
    suspend fun createCart(foodMenu: FoodMenu, totalQuantity: Int) : Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart) : Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart) : Flow<ResultWrapper<Boolean>>
    suspend fun setOrderNotes(item: Cart) : Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
}

class CartRepositoryImpl(private val dataSource: CartDataSource): CartRepository{

    override fun getCartData(): Flow<ResultWrapper<Pair<List<FoodMenuCart>, Double>>> {
        return dataSource.getAllCarts()
            .map {
                proceed {
                    val result = it.toCartFoodMenuList()
                    val totalPrice = result.sumOf{
                        val pricePerItem = it.foodMenuCart.price
                        val itemQuantity = it.cart.itemQuantity
                        pricePerItem * itemQuantity
                    }
                    Pair(result,totalPrice)
                }
            }.map {
                if(it.payload?.first?.isEmpty() == true)
                    ResultWrapper.Empty(it.payload)
                else
                    it
            }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override suspend fun createCart(foodMenu: FoodMenu, totalQuantity: Int): Flow<ResultWrapper<Boolean>> {
        return foodMenu.id?.let { foodId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(foodId = foodId, itemQuantity = totalQuantity)
                )
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Menu Id not found")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }

    }

    override suspend fun setOrderNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0}
    }

}