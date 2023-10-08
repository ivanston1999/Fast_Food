package com.android.fastfood.model

import androidx.room.ColumnInfo

data class Cart(
    var id: Int = 0,
    var foodId: Int = 0,
    var itemQuantity: Int = 0,
    var orderNotes: String? = null
)

