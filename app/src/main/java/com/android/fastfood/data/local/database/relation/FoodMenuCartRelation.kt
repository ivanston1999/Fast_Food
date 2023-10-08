package com.android.fastfood.data.local.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.data.local.database.entitiy.FoodMenuEntity


data class FoodMenuCartRelation(
    @Embedded
    val cart: CartEntity,

    @Relation(parentColumn = "food_id", entityColumn = "id")
    val foodMenu: FoodMenuEntity
)
