package com.android.fastfood.presentation.fragmentcart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import com.android.fastfood.data.repository.CartRepository
import com.android.fastfood.model.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    val cartList = repo.getCartData().asLiveData(Dispatchers.IO)

    fun increaseCart(item: Cart) {
        viewModelScope.launch { repo.increaseCart(item).collect() }
    }

    fun decreaseCart(item: Cart) {
        viewModelScope.launch {repo.decreaseCart(item).collect()}
    }

    fun removeCart(item: Cart) {
        viewModelScope.launch { repo.deleteCart(item).collect() }
    }

    fun setCartNotes(item: Cart)
    {
        viewModelScope.launch { repo.setOrderNotes(item).collect() }
    }


}