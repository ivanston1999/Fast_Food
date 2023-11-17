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
import com.android.fastfood.databinding.ItemCartMenuOrderBinding
import com.android.fastfood.databinding.ItemOrderBinding
import com.android.fastfood.model.Cart
import com.android.fastfood.utils.currecyFormat
import com.android.fastfood.utils.doneEditing


class CartListAdapter(private val cartListener: CartListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Cart>() {
            override fun areItemsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean
            {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean
            {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    fun submitData(data: List<Cart>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (cartListener != null) CartViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), cartListener
        ) else CartOrderViewHolder(
            ItemCartMenuOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }

}

class CartViewHolder(
    private val binding: ItemCartBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item:Cart) {
        setCartData(item)
        setCartNotes(item)

    }

    private fun setCartData(it: Cart) {
        with(binding) {
            binding.ivFood.load(it.imgFoodUrl) {
                crossfade(true)
            }
            tvCounter.text = it.itemQuantity.toString()
            tvFoodName.text = it.foodName
            tvPriceCart.text = (it.itemQuantity * it.foodPrice).currecyFormat()
        }
    }

    private fun setCartNotes(it: Cart) {
        binding.etOrderNote.setText(it.orderNotes)
        binding.etOrderNote.doneEditing {
            binding.etOrderNote.clearFocus()
            val newItem = it.copy().apply {
                orderNotes = binding.etOrderNote.text.toString().trim()
            }
            cartListener?.onUserDoneEditingNotes(newItem)
        }
    }


}

class CartOrderViewHolder(
    private val binding: ItemCartMenuOrderBinding,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.ivProductImage.load(item.imgFoodUrl) {
                crossfade(true)
            }
            tvTotalQuantity.text =
                itemView.rootView.context.getString(
                    R.string.total_quantity,
                    item.itemQuantity.toString()
                )
            tvProductName.text = item.foodName
            tvProductPrice.text = (item.itemQuantity * item.foodPrice).currecyFormat()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.tvNotes.text = item.orderNotes
    }

}


interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}