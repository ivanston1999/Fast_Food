package com.android.fastfood.presentation.checkoutactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.fastfood.data.repository.CartRepository
import kotlinx.coroutines.Dispatchers

class CheckOutViewModel(private val repo: CartRepository) : ViewModel(){
    val cartList = repo.getCartData().asLiveData(Dispatchers.IO)
}