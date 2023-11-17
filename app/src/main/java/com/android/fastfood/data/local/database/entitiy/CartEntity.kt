package com.android.fastfood.data.local.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity("cart")
data class CartEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo("food_id")
    var foodId: Int = 0,

    @ColumnInfo("item_quantity")
    var itemQuantity: Int = 0,

    @ColumnInfo("order_notes")
    var orderNotes: String? = null,

    @ColumnInfo("food_name")
    var foodName: String,

    @ColumnInfo("food_price")
    var foodPrice: Int,

    @ColumnInfo("img_food_url")
    var imgFoodUrl: String
    )