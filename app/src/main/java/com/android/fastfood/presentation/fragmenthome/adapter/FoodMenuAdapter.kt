package com.android.fastfood.presentation.fragmenthome.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.fastfood.core.ViewHolderBinder
import com.android.fastfood.databinding.ItemGridListBinding
import com.android.fastfood.databinding.ItemLinearListBinding
import com.android.fastfood.model.FoodMenu

class FoodMenuAdapter(
    var layoutGridLinear: LayoutGridLinear,
    private val onClickListener: (FoodMenu) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer = AsyncListDiffer(this,object : DiffUtil.ItemCallback<FoodMenu>(){
        override fun areItemsTheSame(oldItem: FoodMenu, newItem: FoodMenu): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: FoodMenu, newItem: FoodMenu): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    })


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LayoutGridLinear.GRID.ordinal -> {
                LinearViewHolder(
                    binding = ItemLinearListBinding.inflate(
                        LayoutInflater.from(parent.context),parent,false
                    ),onClickListener
                )
            }
            else -> {
                GridViewHolder(
                    binding = ItemGridListBinding.inflate(
                        LayoutInflater.from(parent.context),parent,false
                    ),onClickListener
                )
            }
        }

    }
    override fun getItemViewType(position: Int): Int {
        return layoutGridLinear.ordinal
    }
    fun sendData(data : List<FoodMenu>){
        dataDiffer.submitList(data)
    }

    fun refreshList() {
        notifyItemRangeChanged(0,dataDiffer.currentList.size)
    }
    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<FoodMenu>).bind(dataDiffer.currentList[position])
    }
}