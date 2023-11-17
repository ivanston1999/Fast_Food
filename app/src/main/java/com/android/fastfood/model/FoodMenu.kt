package com.android.fastfood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class FoodMenu(
    val id: Int? = null,
    val foodName: String,
    val price: Int,
    val priceFormat: String,
    val description: String,
    val location: String,
    val imgFoodUrl: String,

    ) : Parcelable
