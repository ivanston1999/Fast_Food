package com.android.fastfood.presentation.checkoutactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.android.fastfood.R
import com.android.fastfood.data.local.database.AppDatabase
import com.android.fastfood.data.local.database.datasource.CartDataSource
import com.android.fastfood.data.local.database.datasource.CartDatabaseDataSource
import com.android.fastfood.data.repository.CartRepository
import com.android.fastfood.data.repository.CartRepositoryImpl
import com.android.fastfood.presentation.common.CartListAdapter
import com.android.fastfood.utils.GenericViewModelFactory
import com.android.fastfood.utils.proceedWhen
import com.android.fastfood.databinding.ActivityChekOutBinding
import com.android.fastfood.utils.currecyFormat

class CheckOutActivity : AppCompatActivity() {
    private val viewModel: CheckOutViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource)
        GenericViewModelFactory.create(CheckOutViewModel(repo))
    }

    private val binding : ActivityChekOutBinding by lazy {
        ActivityChekOutBinding.inflate(layoutInflater)
    }

    private val adapter : CartListAdapter by lazy {
        CartListAdapter()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        observeData()
        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }


    private fun setupList() {
        binding.layoutContent.rvCart.adapter = adapter
    }
    private fun observeData() {
        viewModel.cartList.observe(this) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = true
                binding.layoutContent.rvCart.isVisible = true
                binding.cvSectionOrder.isVisible = true
                it.payload?.let { (carts, totalPrice) ->
                    adapter.submitData(carts)
                    binding.tvTotalPrice.text = totalPrice.currecyFormat()
                }
            },
                doOnLoading = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = true
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            },
                doOnError = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = it.exception?.message.orEmpty()
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
                Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
            },
                doOnEmpty = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = getString(R.string.empty)
                it.payload?.let { (_, totalPrice) ->
                    binding.tvTotalPrice.text = totalPrice.currecyFormat()
                }
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            })
        }
    }

}