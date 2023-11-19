package com.android.fastfood.data.network

import com.android.fastfood.data.network.api.datasource.FoodMenuApiDataSource
import com.android.fastfood.data.network.api.datasource.FoodMenuDataSource
import com.android.fastfood.data.network.api.model.category.CategoriesResponse
import com.android.fastfood.data.network.api.model.menu.MenusResponse
import com.android.fastfood.data.network.api.model.order.OrderRequest
import com.android.fastfood.data.network.api.model.order.OrderResponse
import com.android.fastfood.data.network.api.service.FastFoodApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FoodMenuApiDataSourceTest {

    @MockK
    lateinit var service: FastFoodApiService

    private lateinit var dataSource: FoodMenuDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FoodMenuApiDataSource(service)
    }

    @Test
    fun getProducts() {
        runTest {
            val mockResponse = mockk<MenusResponse>(relaxed = true)
            coEvery { service.getProducts(any()) } returns mockResponse
            val response = dataSource.getProducts("minuman")
            coVerify { service.getProducts(any()) }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val response = dataSource.getCategories()
            coVerify { service.getCategories() }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            coEvery { service.createOrder(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            coVerify { service.createOrder(any()) }
            assertEquals(response, mockResponse)
        }
    }
}
