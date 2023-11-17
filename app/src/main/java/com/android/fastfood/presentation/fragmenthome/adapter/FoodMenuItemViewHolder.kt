package com.android.fastfood.presentation.fragmenthome.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.android.fastfood.core.ViewHolderBinder
import com.android.fastfood.databinding.ItemGridListBinding
import com.android.fastfood.databinding.ItemLinearListBinding
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.utils.currecyFormat


private fun Double.formatCurrency(currencySymbol: String): String {
    val formattedAmount = String.format("%, .0f",this).replace(",",".")
    return "$currencySymbol $formattedAmount"
}

class LinearViewHolder(
    private val binding : ItemLinearListBinding,
    private val onClickListener : (FoodMenu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<FoodMenu> {

    override fun bind(item: FoodMenu) {
        binding.ivMenu.load(item.imgFoodUrl){
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.tvMenu.text = item.foodName
        binding.menuPrice.text = item.price.currecyFormat()
        binding.root.setOnClickListener{
            onClickListener.invoke(item)
        }
    }
}


class GridViewHolder(
    private val binding : ItemGridListBinding,
    private val onClickListener : (FoodMenu) -> Unit
) : RecyclerView.ViewHolder(binding.root),ViewHolderBinder<FoodMenu>{

    override fun bind(item: FoodMenu) {
        binding.ivMenu.load(item.imgFoodUrl){
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.tvMenuName.text = item.foodName
        binding.tvPrice.text = item.price.currecyFormat()
        binding.root.setOnClickListener{
            onClickListener.invoke(item)
        }
    }
}