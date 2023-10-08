package com.android.fastfood.data.repository

import com.android.fastfood.data.dummy.CategoryDataSource
import com.android.fastfood.data.local.database.datasource.FoodMenuDataSource
import com.android.fastfood.data.local.database.mapper.toFoodMenuList
import com.android.fastfood.model.Category
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.utils.ResultWrapper
import com.android.fastfood.utils.proceed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface FoodMenuRepository {

    fun getCategories(): List<Category>
    fun getFoodMenu(): Flow<ResultWrapper<List<FoodMenu>>>

}

class FoodMenuRepositoryImpl(
    private val foodMenuDataSource: FoodMenuDataSource,
    private val dummyCategoryDataSource: CategoryDataSource,

    ) : FoodMenuRepository {
    override fun getCategories(): List<Category> {

        return dummyCategoryDataSource.getCategory()
    }

    override fun getFoodMenu(): Flow<ResultWrapper<List<FoodMenu>>> {

        return foodMenuDataSource.getAllFoodMenu().map {
            proceed { it.toFoodMenuList() }
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }

    }


}