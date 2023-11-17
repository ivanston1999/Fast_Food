package com.android.fastfood.presentation.checkoutactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.fastfood.data.repository.CartRepository
import com.android.fastfood.presentation.auth.firebase.repository.UserRepository
import com.android.fastfood.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckOutViewModel(private val cartRepository: CartRepository,
                        private val userRepository: UserRepository
) : ViewModel() {
    val cartList = cartRepository.getUserCartData().asLiveData(Dispatchers.IO)

    private val _checkoutResult = MutableLiveData<ResultWrapper<Boolean>>()
    val checkoutResult : LiveData<ResultWrapper<Boolean>>
        get() = _checkoutResult


    fun createOrder(){
        viewModelScope.launch(Dispatchers.IO) {
            val carts = cartList.value?.payload?.first ?: return@launch
            val total = cartList.value?.payload?.second?.toInt() ?: 0
            val username = userRepository.getCurrentUser()?.fullName.orEmpty()
            cartRepository.createOrder(carts, total, username).collect{
                _checkoutResult.postValue(it)
            }
        }
    }

    fun clearCart(){
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAllCarts()
        }
    }
}