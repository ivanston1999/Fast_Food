package com.android.fastfood.presentation.fragmentdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import com.android.fastfood.R
import com.android.fastfood.databinding.FragmentDetailBinding
import com.android.fastfood.model.FoodMenu


class DetailFragment : Fragment() {

    private fun Double.formatCurrency(currencySymbol: String): String {
        val formattedAmount = String.format("%, .0f",this).replace(",",".")
        return "$currencySymbol $formattedAmount"
    }

    private lateinit var binding : FragmentDetailBinding

    private val foodMenu : FoodMenu? by lazy {
        DetailFragmentArgs.fromBundle(arguments as Bundle).FoodMenu!!
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        viewDetail()
        inDecreaseTotal()
    }

    private fun inDecreaseTotal() {
        var count : Int = 1
        var totalBuy : Double
        val decrease = binding.ivMinus
        val increase = binding.ivAdd
        val textTotalProduct = binding.counter
        val textTotalPrice = binding.btnBuy
        increase.setOnClickListener{
            count += 1
            totalBuy = (count * (foodMenu?.price?.toInt() ?: 0)).toDouble()
            textTotalProduct.text = count.toString()
            textTotalPrice.text = getString(R.string.string_hitung, totalBuy.toInt())
        }
        decrease.setOnClickListener{
            if (count <= 1){
                count = 1
            } else {
                count -= 1
                totalBuy = (count * (foodMenu?.price?.toInt() ?: 0)).toDouble()
                textTotalProduct.text = count.toString()
                textTotalPrice.text = getString(R.string.string_hitung, totalBuy.toInt())
            }
        }
    }


    private fun viewDetail() {
        foodMenu?.let {
            binding.apply {
                ivFoodMenu.load(it?.imgFoodUrl){
                    crossfade(true)
                }
                tvMenuName.text = it?.foodName
                tvPriceMenu.text= it?.price?.formatCurrency("Rp. ")
                tvDesc.apply{
                    text = it?.description
                    movementMethod = ScrollingMovementMethod()
                }
                tvLocation.text = it?.location
                btnBuy.text =
                    getString(R.string.string_hitung, it?.price?.toInt())
            }
        }


    }

    private fun setClickListener() {

        binding.tvLocation.setOnClickListener {
            navigateToMaps()
        }
    }

    private fun navigateToMaps() {

        val location = foodMenu?.location
        val intentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location))
        val intent = Intent(Intent.ACTION_VIEW, intentUri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(requireContext().packageManager) == null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
        }
    }
}