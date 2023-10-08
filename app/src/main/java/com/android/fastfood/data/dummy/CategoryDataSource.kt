package com.android.fastfood.data.dummy

import com.android.fastfood.model.Category


interface CategoryDataSource{
    fun getCategory(): List<Category>
}

class CategoryDataSourceImpl: CategoryDataSource {
    override fun getCategory(): List<Category> = listOf(
        Category(
            categoryName = "Burger",
            imgCategoryUrl = "https://images.unsplash.com/photo-1551782450-a2132b4ba21d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fGJ1cmdlcnxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=600&q=60"
        ),

        Category(
            categoryName = "Mie",
            imgCategoryUrl = "https://images.unsplash.com/photo-1612927601601-6638404737ce?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1887&q=80"
        ),

        Category(
            categoryName = "Snack",
            imgCategoryUrl = "https://images.unsplash.com/photo-1575549595260-623d27ba5e44?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8c25hY2slMjBmb29kfGVufDB8fDB8fHww&auto=format&fit=crop&w=600&q=60"
        ),

        Category(
            categoryName = "Minuman",
            imgCategoryUrl = "https://images.unsplash.com/photo-1627053947185-1d02dc9435ac?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1854&q=80"
        )


    )


}