package com.android.fastfood.data.local.database.mapper

import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.model.Cart

fun CartEntity?.toCart() = Cart(
    id = this?.id ?: 0,
    foodId = this?.foodId ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    orderNotes = this?.orderNotes.orEmpty(),
    foodName = this?.foodName.orEmpty(),
    foodPrice = this?.foodPrice ?: 0,
    imgFoodUrl = this?.imgFoodUrl.orEmpty()

)


fun Cart.toCartEntity() = CartEntity(
    id = this?.id ?: 0,
    foodId = this?.foodId ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    orderNotes = this?.orderNotes.orEmpty(),
    foodName = this?.foodName.orEmpty(),
    foodPrice = this?.foodPrice ?: 0,
    imgFoodUrl = this?.imgFoodUrl.orEmpty(),
)


fun List<CartEntity?>.toCartList() = this.map { it.toCart() }

