package com.android.fastfood.presentation.detailactivity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import coil.load
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.android.fastfood.databinding.ActivityDetailBinding
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.utils.GenericViewModelFactory
import com.android.fastfood.utils.currecyFormat
import com.android.fastfood.utils.proceedWhen
import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailViewModel by viewModel {
        parametersOf(intent.extras ?: bundleOf())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindMenu(viewModel?.foodMenu)
        observeData()
        setClickListener()

    }

    private fun setClickListener() {

        binding.ivMinus.setOnClickListener{
            viewModel.minus()
        }
        binding.ivAdd.setOnClickListener{
            viewModel.add()
        }
        binding.tvLocation.setOnClickListener {
            viewModel.onLocationClicked()
        }
        binding.btnAddCart.setOnClickListener {
            viewModel.addToCart()
        }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this){
            binding.tvTotalPrice.text = it.toString()
        }
        viewModel.menuCountLiveData.observe(this){
            binding.counter.text = it.toString()
        }
        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                }, doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                })
        }
        viewModel.navigateToMapsLiveData.observe(this) { location ->
            location?.let {
                navigateToMaps(location)
            }
        }
    }

    private fun bindMenu(foodMenu: FoodMenu?) {
        foodMenu?.let { item ->
            binding.ivFoodMenu.load(item.imgFoodUrl) {
                crossfade(true)
            }
            binding.tvMenuName.text = item.foodName
            binding.tvDesc.text = item.description
            binding.tvPriceMenu.text = item.price.currecyFormat()
            binding.tvLocation.text = item.location
        }
    }

    private fun navigateToMaps(location: String) {
        val gmmIntentUri = Uri.parse("http://maps.google.com/?q=$location")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    companion object {
        const val FOOD_DETAIL = "food_detail"
        fun toDetail(context: Context, foodMenu: FoodMenu) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(FOOD_DETAIL, foodMenu)
            context.startActivity(intent)
        }
    }
}