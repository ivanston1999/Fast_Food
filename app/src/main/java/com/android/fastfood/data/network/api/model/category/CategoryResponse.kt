package com.android.fastfood.data.network.api.model.category

import androidx.annotation.Keep
import com.android.fastfood.model.Category

import com.google.gson.annotations.SerializedName

@Keep
data class CategoryResponse(
    @SerializedName("image_url")
    val imgCategoryUrl: String?,
    @SerializedName("nama")
    val categoryName: String?,

)

fun CategoryResponse.toCategory() = Category(
    imgCategoryUrl = this.imgCategoryUrl.orEmpty(),
    categoryName = this.categoryName.orEmpty(),
)

fun Collection<CategoryResponse>.toCategoryList() = this.map {
    it.toCategory()
}