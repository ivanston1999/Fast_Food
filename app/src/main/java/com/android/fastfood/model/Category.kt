package com.android.fastfood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Category(
    val id: String = UUID.randomUUID().toString(),
    val categoryName: String,
    val imgCategoryUrl: String,
) : Parcelable
