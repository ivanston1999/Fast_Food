package com.android.fastfood.presentation.detailactivity

import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.fastfood.data.repository.CartRepository
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.utils.ResultWrapper
import kotlinx.coroutines.launch

class DetailViewModel (private val extras: Bundle?, private val cartRepository: CartRepository
) : ViewModel() {

    val foodMenu = extras?.getParcelable<FoodMenu>(DetailActivity.FOOD_DETAIL)

    val priceLiveData = MutableLiveData<Int>().apply {
        postValue(0)
    }
    val menuCountLiveData = MutableLiveData<Int>().apply {
        postValue(0)
    }
    private val _addToCartResult = MutableLiveData<ResultWrapper<Boolean>>()
    val addToCartResult: LiveData<ResultWrapper<Boolean>>
        get() = _addToCartResult


    val navigateToMapsLiveData = MutableLiveData<String?>()

    fun onLocationClicked() {
        val location = foodMenu?.location
        navigateToMapsLiveData.value = location
    }


    fun add() {
        val count = (menuCountLiveData.value ?: 0) + 1
        menuCountLiveData.postValue(count)
        priceLiveData.postValue(foodMenu?.price?.times(count) ?: 0)
    }

    fun minus() {
        if((menuCountLiveData.value ?: 0) > 0){
            val count = (menuCountLiveData.value ?: 0) -1
            menuCountLiveData.postValue(count)
            priceLiveData.postValue(foodMenu?.price?.times(count) ?: 0)
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val menuQuantity =
                if ((menuCountLiveData.value ?: 0) <= 0) 1 else menuCountLiveData.value ?: 0
            foodMenu?.let {
                cartRepository.createCart(it, menuQuantity).collect { result ->
                    _addToCartResult.postValue(result)
                }
            }
        }
    }}