package com.android.fastfood.model

import androidx.room.ColumnInfo
import com.android.fastfood.data.network.api.model.order.OrderItemRequest

data class Cart(
    var id: Int = 0,
    var foodId: Int = 0,
    var itemQuantity: Int = 0,
    var orderNotes: String? = null,
    var foodName: String,
    var foodPrice: Int,
    var imgFoodUrl: String
)

fun Cart.toOrderItemRequest() = OrderItemRequest(
    notes = this.orderNotes.orEmpty(),
    price = this.foodPrice,
    name = this.foodName,
    qty = this.itemQuantity
)

fun Collection<Cart>.toOrderItemRequestList() = this.map { it.toOrderItemRequest() }

