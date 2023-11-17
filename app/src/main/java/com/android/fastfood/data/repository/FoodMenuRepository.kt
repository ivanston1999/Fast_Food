package com.android.fastfood.data.repository

import android.view.Menu
import com.android.fastfood.data.network.api.datasource.FoodMenuDataSource
import com.android.fastfood.data.network.api.model.category.toCategoryList
import com.android.fastfood.data.network.api.model.menu.toMenuList
import com.android.fastfood.model.Category
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.utils.ResultWrapper
import com.android.fastfood.utils.proceedFlow
import kotlinx.coroutines.flow.Flow


interface FoodMenuRepository {

    fun getCategories(): Flow<ResultWrapper<List<Category>>>
    fun getFoodMenu(category: String? = null): Flow<ResultWrapper<List<FoodMenu>>>

}

class FoodMenuRepositoryImpl(
    private val apiDataSource: FoodMenuDataSource,
) : FoodMenuRepository {

    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            apiDataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }

    override fun getFoodMenu(category: String?): Flow<ResultWrapper<List<FoodMenu>>> {
        return proceedFlow {
            apiDataSource.getProducts(category).data?.toMenuList() ?: emptyList()
        }
    }
}