package com.android.fastfood.data.local.database.mapper

import android.view.Menu
import com.android.fastfood.data.local.database.entitiy.FoodMenuEntity
import com.android.fastfood.model.FoodMenu

fun FoodMenuEntity?.toMenu() = FoodMenu(
    id = this?.id ?: 0,
    foodName = this?.foodName.orEmpty(),
    price = this?.price ?:0.0,
    imgFoodUrl = this?.imgFoodUrl.orEmpty(),
    description = this?.description.orEmpty(),
    location = this?.location.orEmpty()

)

fun FoodMenu?.toFoodMenuEntity() = FoodMenuEntity(
    foodName = this?.foodName.orEmpty(),
    price = this?.price ?:0.0,
    imgFoodUrl = this?.imgFoodUrl.orEmpty(),
    description = this?.description.orEmpty(),
    location = this?.location.orEmpty()

).apply {
    this@toFoodMenuEntity?.id?.let {
        this.id = this@toFoodMenuEntity.id
    }
}

fun List<FoodMenuEntity?>.toFoodMenuList() = this.map { it.toMenu() }
fun List<FoodMenu?>.toFoodMenuEntity() = this.map {
    val toMenuEntity = it.toFoodMenuEntity()
    toMenuEntity
}