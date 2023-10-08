package com.android.fastfood.data.local.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("foodMenu")
data class FoodMenuEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo("name")
    val foodName: String,

    @ColumnInfo("price")
    val price: Double,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("location")
    val location: String,

    @ColumnInfo("img_food_url")
    val imgFoodUrl: String,
)