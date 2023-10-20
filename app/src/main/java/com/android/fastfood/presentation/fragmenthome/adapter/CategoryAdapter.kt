package com.android.fastfood.presentation.fragmenthome.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.android.fastfood.databinding.ItemCategoryBinding
import com.android.fastfood.model.Category

class CategoryAdapter() : RecyclerView.Adapter<CategoryViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }
    })

    fun submitData(data: List<Category>) {
        differ.submitList(data)
        notifyItemChanged(0, data.size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        return CategoryViewHolder(
            binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
    }



class CategoryViewHolder(
    private val binding: ItemCategoryBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Category) {
        binding.ivCategory
            .load(item.imgCategoryUrl){
            transformations(CircleCropTransformation())}
        binding.tvCategoryName.text = item.categoryName
    }

}