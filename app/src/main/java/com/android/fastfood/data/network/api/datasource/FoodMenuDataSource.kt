package com.android.fastfood.data.network.api.datasource

import com.android.fastfood.data.network.api.model.category.CategoriesResponse
import com.android.fastfood.data.network.api.model.menu.MenusResponse
import com.android.fastfood.data.network.api.model.order.OrderRequest
import com.android.fastfood.data.network.api.model.order.OrderResponse
import com.android.fastfood.data.network.api.service.FastFoodApiService


interface FoodMenuDataSource {
    suspend fun getProducts(category: String? = null): MenusResponse
    suspend fun getCategories(): CategoriesResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class FoodMenuApiDataSource(private val service: FastFoodApiService) : FoodMenuDataSource {
    override suspend fun getProducts(category: String?): MenusResponse {
        return service.getProducts(category)
    }

    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }

}