package com.android.fastfood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class FoodMenu(
    val id: String = UUID.randomUUID().toString(),
    val foodName: String,
    val price: Double,
    val description: String,
    val location: String,
    val imgFoodUrl: String,

    ) : Parcelable
