package com.android.fastfood.presentation.fragmentcart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.android.fastfood.R
import com.android.fastfood.data.local.database.AppDatabase
import com.android.fastfood.data.local.database.datasource.CartDataSource
import com.android.fastfood.data.local.database.datasource.CartDatabaseDataSource
import com.android.fastfood.data.repository.CartRepository
import com.android.fastfood.data.repository.CartRepositoryImpl
import com.android.fastfood.databinding.FragmentCartBinding
import com.android.fastfood.model.Cart
import com.android.fastfood.presentation.checkoutactivity.CheckOutActivity
import com.android.fastfood.presentation.common.CartListAdapter
import com.android.fastfood.presentation.common.CartListener
import com.android.fastfood.utils.GenericViewModelFactory
import com.android.fastfood.utils.currecyFormat
import com.android.fastfood.utils.hideKeyboard
import com.android.fastfood.utils.proceedWhen


class CartFragment : Fragment() {


    private lateinit var binding: FragmentCartBinding

    private val viewModel: CartViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource)
        GenericViewModelFactory.create(CartViewModel(repo))
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter(object : CartListener {
            override fun onPlusTotalItemCartClicked(cart: Cart) {
                viewModel.increaseCart(cart)
            }

            override fun onMinusTotalItemCartClicked(cart: Cart) {
                viewModel.decreaseCart(cart)
            }

            override fun onRemoveCartClicked(cart: Cart) {
                viewModel.removeCart(cart)
            }

            override fun onUserDoneEditingNotes(cart: Cart) {
                viewModel.setCartNotes(cart)
                hideKeyboard()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        observeData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnCheckout.setOnClickListener {
            context?.startActivity(Intent(requireContext(), CheckOutActivity::class.java))
        }
    }

    private fun setupList() {
        binding.rvCart.itemAnimator = null
        binding.rvCart.adapter = adapter
    }

    private fun observeData()  {
        viewModel.cartList.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess =
                {
                    binding.rvCart.isVisible = true
                    binding.layoutState.root.isVisible= false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    result.payload?.let{ (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.tvTotalPrice.text = totalPrice.currecyFormat()
                    }
                },
                doOnError =
                {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = it.exception?.message.orEmpty()
                    binding.layoutState.pbLoading.isVisible = false
                },
                doOnLoading =
                {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutState.pbLoading.isVisible = true
                    binding.rvCart.isVisible = false
                },
                doOnEmpty =
                {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.empty)
                    it.payload?.let { (_, totalPrice) ->
                        binding.tvTotalPrice.text = totalPrice.currecyFormat()
                    }
                    binding.rvCart.isVisible = false
                })
        }
    }

}