package com.android.fastfood.presentation.fragmentcart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import com.android.fastfood.data.repository.CartRepository
import com.android.fastfood.model.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    val cartList = repo.getUserCartData().asLiveData(Dispatchers.IO)

    fun decreaseCart(item: Cart) {
        viewModelScope.launch {
            repo.decreaseCart(item).collect {
                Log.d("CartViewModel", " : Decrease Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }

    fun increaseCart(item: Cart) {
        viewModelScope.launch {
            repo.increaseCart(item).collect {
                Log.d("CartViewModel", " : Increase Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }
    fun removeCart(item: Cart) {
        viewModelScope.launch {
            repo.deleteCart(item).collect {
                Log.d("CartViewModel", " : Remove Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }

    fun cartNotEmpty(): Boolean {
        return cartList.value?.payload?.first?.isNotEmpty() ?: false
    }

    fun setCartNotes(item: Cart) {
        viewModelScope.launch { repo.setCartNotes(item).collect() }
    }

}