package com.android.fastfood.presentation.fragmenthome

import android.view.Menu
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.fastfood.data.repository.FoodMenuRepository
import com.android.fastfood.model.Category
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.utils.AssetWrapper
import com.android.fastfood.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FoodMenuRepository,
                    private val assetWrapper: AssetWrapper
) : ViewModel() {



    private val _categories = MutableLiveData<ResultWrapper<List<Category>>>()
    private val _menus = MutableLiveData<ResultWrapper<List<FoodMenu>>>()

    val categories : LiveData<ResultWrapper<List<Category>>>
        get() = _categories
    val menus : LiveData<ResultWrapper<List<FoodMenu>>>
        get() = _menus



    fun getCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories().collect{
                _categories.postValue(it)
            }
        }
    }

    fun getMenus(category: String? = null){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFoodMenu(if(category == "all") null else category?.lowercase()).collect{
                _menus.postValue(it)
            }
        }
    }

    val menuListLiveData = repository.getFoodMenu().asLiveData(
        Dispatchers.IO)

}