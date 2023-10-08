package com.android.fastfood.presentation.fragmenthome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.fastfood.data.dummy.CategoryDataSource
import com.android.fastfood.data.dummy.CategoryDataSourceImpl
import com.android.fastfood.data.dummy.FoodMenuDataSource
import com.android.fastfood.data.dummy.FoodMenuDataSourceImpl
import com.android.fastfood.databinding.FragmentHomeBinding
import com.android.fastfood.model.Category
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.presentation.detailactivity.DetailActivity
import com.android.fastfood.presentation.fragmenthome.adapter.CategoryAdapter
import com.android.fastfood.presentation.fragmenthome.adapter.FoodMenuAdapter
import com.android.fastfood.presentation.fragmenthome.adapter.LayoutGridLinear


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val dataSource: FoodMenuDataSource by lazy {
        FoodMenuDataSourceImpl()
    }

    private val adapter: FoodMenuAdapter by lazy {
        FoodMenuAdapter(
            layoutGridLinear = LayoutGridLinear.LINEAR,
            onClickListener = { navigateToDetail(it) }
        )
    }

    private fun navigateToDetail(food: FoodMenu) {
        DetailActivity.toDetail(requireContext(), food)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearOrGrid()
        switchLinearGrid()
        categoryList()



    }

    private fun categoryList() {
        val categoryAdapter = CategoryAdapter()
        binding.rvCategory.adapter = categoryAdapter
        binding.rvCategory.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        val categoryDataSource: CategoryDataSource = CategoryDataSourceImpl()
        val categoryList: List<Category> = categoryDataSource.getCategory()
        categoryAdapter.submitData(categoryList)

    }

    private fun linearOrGrid() {
        val span = if (adapter.layoutGridLinear == LayoutGridLinear.LINEAR) 1 else 2
        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(requireContext(), span)
            adapter = this@HomeFragment.adapter
        }
        adapter.sendData(dataSource.getFood())

    }

    private fun switchLinearGrid() {
        binding.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            (binding.rvMenu.layoutManager as GridLayoutManager).spanCount = if (isChecked) 2 else 1
            adapter.layoutGridLinear =
                if (isChecked) LayoutGridLinear.GRID else LayoutGridLinear.LINEAR
            adapter.refreshList()
        }

    }


}