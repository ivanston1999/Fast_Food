package com.android.fastfood.data.repository

import app.cash.turbine.test
import com.android.fastfood.data.network.api.datasource.FoodMenuDataSource
import com.android.fastfood.data.network.api.model.category.CategoriesResponse
import com.android.fastfood.data.network.api.model.category.CategoryResponse
import com.android.fastfood.data.network.api.model.menu.MenuItemResponse
import com.android.fastfood.data.network.api.model.menu.MenusResponse
import com.android.fastfood.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuRepositoryImplTest {

    @MockK
    lateinit var remoteDataSource: FoodMenuDataSource

    private lateinit var repository: FoodMenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = FoodMenuRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get Categories, with result loading`() {
        val mockCategoryResponse = mockk<CategoriesResponse>()
        runTest {
            coEvery { remoteDataSource.getCategories() } returns mockCategoryResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get Categories, with result success`() {
        val fakeCategoryResponse = CategoryResponse(
            imgCategoryUrl = "url",
            categoryName = "name"
        )
        val fakeCategoriesResponse = CategoriesResponse(
            code = 100,
            status = true,
            message = "Success",
            data = listOf(fakeCategoryResponse)
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get Categories, with result empty`() {
        val fakeCategoriesResponse = CategoriesResponse(
            code = 100,
            status = true,
            message = "Success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get Categories, with result error`() {
        runTest {
            coEvery { remoteDataSource.getCategories() } throws IllegalStateException("Mock error")
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get Menus, with result loading`() {
        val mockMenuResponse = mockk<MenusResponse>()
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns mockMenuResponse
            repository.getFoodMenu("Snack").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get Menus, with result success`() {
        val fakeMenuItemResponse = MenuItemResponse(
            menuImgUrl = "url",
            name = "name",
            price = 11000,
            priceFormat = "format",
            desc = "desc",
            location = "location"
        )
        val fakeMenuResponse = MenusResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeMenuItemResponse)
        )
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns fakeMenuResponse
            repository.getFoodMenu("Snack").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get Menus, with result empty`() {
        val fakeMenuResponse = MenusResponse(
            code = 200,
            status = true,
            message = "Success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns fakeMenuResponse
            repository.getFoodMenu().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get Menus, with result error`() {
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } throws IllegalStateException("Mock error")
            repository.getFoodMenu().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }
}
