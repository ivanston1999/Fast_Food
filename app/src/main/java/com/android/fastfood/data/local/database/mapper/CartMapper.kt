package com.android.fastfood.data.local.database.mapper

import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.data.local.database.relation.FoodMenuCartRelation
import com.android.fastfood.model.Cart
import com.android.fastfood.model.FoodMenuCart

fun CartEntity?.toCart() = Cart(
    id = this?.id ?: 0,
    foodId = this?.foodId ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    orderNotes = this?.orderNotes.orEmpty()
)


fun Cart.toCartEntity() = CartEntity(
    id = this?.id ?: 0,
    foodId = this?.foodId ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    orderNotes = this?.orderNotes.orEmpty()
)


fun List<CartEntity?>.toCartList() = this.map { it.toCart() }

fun FoodMenuCartRelation.toCartFoodMenu() = FoodMenuCart(
    cart = this.cart.toCart(),
    foodMenuCart = this.foodMenu.toMenu()
)

fun List<FoodMenuCartRelation>.toCartFoodMenuList() = this.map {it.toCartFoodMenu()}