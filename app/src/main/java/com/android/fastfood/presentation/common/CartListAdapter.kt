package com.android.fastfood.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.fastfood.R
import com.android.fastfood.core.ViewHolderBinder
import com.android.fastfood.databinding.ItemCartBinding
import com.android.fastfood.databinding.ItemOrderBinding
import com.android.fastfood.model.Cart
import com.android.fastfood.model.FoodMenuCart
import com.android.fastfood.utils.currecyFormat
import com.android.fastfood.utils.doneEditing


class CartListAdapter(private val cartListener: CartListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<FoodMenuCart>() {
            override fun areItemsTheSame(
                oldItem: FoodMenuCart,
                newItem: FoodMenuCart
            ): Boolean
            {
                return oldItem.cart.id == newItem.cart.id && oldItem.foodMenuCart.id == newItem.foodMenuCart.id
            }

            override fun areContentsTheSame(
                oldItem: FoodMenuCart,
                newItem: FoodMenuCart
            ): Boolean
            {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    fun submitData(data: List<FoodMenuCart>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (cartListener != null) CartViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), cartListener
        ) else CartOrderViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<FoodMenuCart>).bind(dataDiffer.currentList[position])
    }

}

class CartViewHolder(
    private val binding: ItemCartBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<FoodMenuCart> {
    override fun bind(item:FoodMenuCart) {
        setCartData(item)
        setCartNotes(item)
        setClickListeners(item)
    }

    private fun setCartData(it: FoodMenuCart) {
        with(binding) {
            binding.ivFood.load(it.foodMenuCart.imgFoodUrl) {
                crossfade(true)
            }
            tvCounter.text = it.cart.itemQuantity.toString()
            tvFoodName.text = it.foodMenuCart.foodName
            tvPriceCart.text = (it.cart.itemQuantity * it.foodMenuCart.price).currecyFormat()
        }
    }

    private fun setCartNotes(it: FoodMenuCart) {
        binding.etOrderNote.setText(it.cart.orderNotes)
        binding.etOrderNote.doneEditing {
            binding.etOrderNote.clearFocus()
            val newItem = it.cart.copy().apply {
                orderNotes = binding.etOrderNote.text.toString().trim()
            }
            cartListener?.onUserDoneEditingNotes(newItem)
        }
    }

    private fun setClickListeners(item: FoodMenuCart) {
        with(binding) {
            ivMinus.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item.cart) }
            ivAdd.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item.cart) }
            ivDelete.setOnClickListener { cartListener?.onRemoveCartClicked(item.cart) }
        }
    }
}

class CartOrderViewHolder(
    private val binding: ItemOrderBinding,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<FoodMenuCart> {
    override fun bind(item: FoodMenuCart) {
        setCartData(item)
        setCartNotes(item)
    }

    private fun setCartData(it: FoodMenuCart) {
        with(binding) {
            binding.ivFood.load(it.foodMenuCart.imgFoodUrl) {
                crossfade(true)
            }
            tvTotalBuy.text =
                itemView.rootView.context.getString(
                    R.string.total_buy,
                    it.cart.itemQuantity.toString()
                )
            tvFoodNameOrder.text = it.foodMenuCart.foodName
            tvPriceOrder.text = (it.cart.itemQuantity * it.foodMenuCart.price).currecyFormat()
        }
    }

    private fun setCartNotes(item: FoodMenuCart) {
        binding.tvOrderNotesOrder.text = item.cart.orderNotes
    }

}


interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}