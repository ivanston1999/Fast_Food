package com.android.fastfood.presentation.detailactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.android.fastfood.databinding.ActivityDetailBinding
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.utils.currecyFormat

class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val foodMenu = intent.getParcelableExtra(FOOD_DETAIL) as? FoodMenu
        if (foodMenu != null) {
            bindData(foodMenu)
        }

    }

    private fun bindData(foodMenu: FoodMenu) {
        binding.ivFoodMenu.load(foodMenu.imgFoodUrl)
        binding.tvMenuName.text = foodMenu.foodName
        binding.tvPriceMenu.text = foodMenu.price.currecyFormat()
        binding.tvDesc.text = foodMenu.description
        binding.tvLocation.text = foodMenu.location
    }


    companion object {
        private const val FOOD_DETAIL = "food_detail"
        fun toDetail(context: Context, food: FoodMenu) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(FOOD_DETAIL, food)
            context.startActivity(intent)
        }
    }
}