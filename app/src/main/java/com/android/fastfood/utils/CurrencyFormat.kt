package com.android.fastfood.utils

import android.icu.text.NumberFormat
import android.icu.util.Currency


fun Double.currecyFormat():String{
    val price = NumberFormat.getCurrencyInstance()
    price.currency = Currency.getInstance("IDR")
    price.maximumFractionDigits = 0

    return price.format(this)
}