package com.android.fastfood.data.repository

import app.cash.turbine.test
import com.android.fastfood.data.local.database.datasource.CartDataSource
import com.android.fastfood.data.local.database.entitiy.CartEntity
import com.android.fastfood.data.network.api.datasource.FoodMenuDataSource
import com.android.fastfood.data.network.api.model.order.OrderResponse
import com.android.fastfood.model.Cart
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {

    @MockK
    lateinit var localDataSource: CartDataSource

    @MockK
    lateinit var remoteDataSource: FoodMenuDataSource

    private lateinit var repository: CartRepository

    private val fakeCartList = listOf(
        CartEntity(
            id = 1,
            foodId = 1,
            foodName = "Sate Cirebon",
            foodPrice = 12000,
            imgFoodUrl = "url",
            itemQuantity = 2,
            orderNotes = "notes"
        ),
        CartEntity(
            id = 2,
            foodId = 1,
            foodName = "Sate Padang",
            foodPrice = 14000,
            imgFoodUrl = "url",
            itemQuantity = 2,
            orderNotes = "notes"
        )
    )

    val mockCart = Cart(
        id = 1,
        foodId = 1,
        foodName = "Sate",
        foodPrice = 12000,
        imgFoodUrl = "url",
        itemQuantity = 2,
        orderNotes = "notes"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CartRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun deleteAllCarts() {
        coEvery { localDataSource.deleteAllCarts() } returns Unit
        runTest {
            val result = repository.deleteAllCarts()
            coVerify { localDataSource.deleteAllCarts() }
            assertEquals(result, Unit)
        }
    }

    @Test
    fun `get user cart data, result success`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.first?.size, 2)
                assertEquals(data.payload?.second, 52000)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user cart data, result loading`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2101)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user cart data, result error`() {
        every { localDataSource.getAllCarts() } returns flow {
            throw IllegalStateException("Mock Error")
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user cart data, result empty`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(listOf())
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `create cart loading, menu id not null`() {
        runTest {
            val mockProduct = mockk<FoodMenu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart success, menu id not null`() {
        runTest {
            val mockProduct = mockk<FoodMenu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    assertEquals(result.payload, true)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart error, menu id not null`() {
        runTest {
            val mockProduct = mockk<FoodMenu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } throws IllegalStateException("Mock error")
            repository.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `decrease cart when quantity less than  or equal 0 `() {
        val mockCart = Cart(
            id = 1,
            foodId = 1,
            foodName = "Sate",
            foodPrice = 12000,
            imgFoodUrl = "url",
            itemQuantity = 0,
            orderNotes = "notes"
        )
        coEvery { localDataSource.deleteCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteCart(any()) }
                coVerify(atLeast = 0) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `decrease cart when quantity more than 0 `() {
        val mockCart = Cart(
            id = 1,
            foodId = 1,
            foodName = "Sate",
            foodPrice = 12000,
            imgFoodUrl = "url",
            itemQuantity = 2,
            orderNotes = "notes"
        )
        coEvery { localDataSource.deleteCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `increase cart`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `set cart notes`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `delete cart`() {
        coEvery { localDataSource.deleteCart(any()) } returns 1
        runTest {
            repository.deleteCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteCart(any()) }
            }
        }
    }

    @Test
    fun `test order`() {
        runTest {
            val mockCarts = listOf(
                Cart(
                    id = 1,
                    foodId = 1,
                    foodName = "Sate",
                    foodPrice = 12000,
                    imgFoodUrl = "url",
                    itemQuantity = 2,
                    orderNotes = "notes"
                ),
                Cart(
                    id = 2,
                    foodId = 2,
                    foodName = "Sate Kambing",
                    foodPrice = 12000,
                    imgFoodUrl = "url",
                    itemQuantity = 2,
                    orderNotes = "notes"
                )
            )
            val totalPrice = 13000
            val username = "pani"
            coEvery { remoteDataSource.createOrder(any()) } returns OrderResponse(
                code = 200,
                message = "Success",
                status = true
            )
            repository.createOrder(mockCarts, totalPrice, username).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Success)
            }
        }
    }
}
