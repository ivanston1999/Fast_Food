package com.android.fastfood.data.network.api.model.menu


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.android.fastfood.model.FoodMenu

@Keep
data class MenuItemResponse(
    @SerializedName("image_url")
    val menuImgUrl: String?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("harga")
    val price: Int?,
    @SerializedName("harga_format")
    val priceFormat: String,
    @SerializedName("detail")
    val desc: String?,
    @SerializedName("alamat_resto")
    val location: String?

)

fun MenuItemResponse.toMenu() = FoodMenu(
    foodName = this.name.orEmpty(),
    price = this.price ?: 0,
    priceFormat = this.priceFormat,
    description = this.desc.orEmpty(),
    location = this.location.orEmpty(),
    imgFoodUrl = this.menuImgUrl.orEmpty()
)

fun Collection<MenuItemResponse>.toMenuList() = this.map { it.toMenu() }