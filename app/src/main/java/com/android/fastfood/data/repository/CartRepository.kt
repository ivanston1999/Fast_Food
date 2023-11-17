package com.android.fastfood.data.repository


import com.android.fastfood.data.local.database.datasource.CartDataSource
import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.data.local.database.mapper.toCartEntity
import com.android.fastfood.data.local.database.mapper.toCartList
import com.android.fastfood.data.network.api.datasource.FoodMenuApiDataSource
import com.android.fastfood.data.network.api.datasource.FoodMenuDataSource
import com.android.fastfood.data.network.api.model.order.OrderRequest
import com.android.fastfood.model.Cart
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.model.toOrderItemRequestList
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

    fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Int>>>
    suspend fun createCart(foodMenu: FoodMenu, totalQuantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAllCarts()
    suspend fun createOrder(items: List<Cart>, totalPrice: Int, username: String): Flow<ResultWrapper<Boolean>>

}
class CartRepositoryImpl(
    private val dataSource: CartDataSource,
    private val apiDataSource: FoodMenuDataSource
) : CartRepository {

    override fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Int>>> {
        return dataSource.getAllCarts().map {
            proceed {
                val cartList = it.toCartList()
                val totalPrice = cartList.sumOf {
                    val quantity = it.itemQuantity
                    val pricePerITem = it.foodPrice
                    quantity * pricePerITem
                }
                Pair(cartList, totalPrice)
            }
        }.map {
            if (it.payload?.first?.isEmpty() == true){
                ResultWrapper.Empty(it.payload)
            } else {
                it
            }
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

    override suspend fun createCart(
        foodMenu: FoodMenu,
        totalQuantity: Int,
    ): Flow<ResultWrapper<Boolean>> {
        return foodMenu.id?.let { foodId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(
                        itemQuantity = totalQuantity,
                        imgFoodUrl = foodMenu.imgFoodUrl,
                        foodName = foodMenu.foodName,
                        foodPrice = foodMenu.price,
                        foodId = foodId,
                    )
                )
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Menu tidak ada")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart((modifiedCart.toCartEntity())) > 0 }
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

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteAllCarts() {
        dataSource.deleteAllCarts()
    }

    override suspend fun createOrder(items: List<Cart>, totalPrice: Int, username: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItem = items.toOrderItemRequestList()
            val orderRequest = OrderRequest(orderItem, totalPrice, username)
            apiDataSource.createOrder(orderRequest).status == true
        }
    }
}