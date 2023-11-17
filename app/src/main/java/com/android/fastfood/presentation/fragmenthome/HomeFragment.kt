package com.android.fastfood.presentation.fragmenthome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.android.fastfood.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.android.fastfood.databinding.FragmentHomeBinding
import com.android.fastfood.model.FoodMenu
import com.android.fastfood.presentation.detailactivity.DetailActivity
import com.android.fastfood.presentation.fragmenthome.adapter.CategoryAdapter
import com.android.fastfood.presentation.fragmenthome.adapter.FoodMenuAdapter
import com.android.fastfood.presentation.fragmenthome.adapter.LayoutGridLinear
import com.android.fastfood.utils.proceedWhen


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

//    private val dataSources: FoodMenuDataSource by lazy {
//        FoodMenuDataSourceImpl()
//    }

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {
            viewModel.getMenus(it.categoryName)
        }
    }

    private val viewModel: HomeViewModel by viewModel()



    private val foodAdapter: FoodMenuAdapter by lazy {
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
        switchLinearGrid()
        observeFoodMenuData()
        observe()
        getData()



    }

    private fun observe() {
        observeData()
        observeFoodMenuData()
    }

    private fun observeData() {
        viewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutStateCategory.root.isVisible = false
                binding.layoutStateCategory.pbLoading.isVisible = false
                binding.layoutStateCategory.tvError.isVisible = false
                binding.rvCategory.apply {
                    isVisible = true
                    adapter = categoryAdapter
                }
                it.payload?.let { data -> categoryAdapter.submitData(data) }
            }, doOnLoading = {
                binding.layoutStateCategory.root.isVisible = true
                binding.layoutStateCategory.pbLoading.isVisible = true
                binding.layoutStateCategory.tvError.isVisible = false
                binding.rvCategory.isVisible = false
            }, doOnError = {
                binding.layoutStateCategory.root.isVisible = true
                binding.layoutStateCategory.pbLoading.isVisible = false
                binding.layoutStateCategory.tvError.isVisible = true
                binding.layoutStateCategory.tvError.text = it.exception?.message.orEmpty()
                binding.rvCategory.isVisible = false
            })
        }
        viewModel.menus.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                binding.rvMenu.smoothScrollToPosition(0)
                binding.layoutStateMenu.root.isVisible = false
                binding.layoutStateMenu.pbLoading.isVisible = false
                binding.layoutStateMenu.tvError.isVisible = false
                binding.rvMenu.apply {
                    isVisible = true
                    adapter = foodAdapter
                }
                it.payload?.let { data -> foodAdapter.submitData(data) }
            }, doOnLoading = {
                binding.layoutStateMenu.root.isVisible = true
                binding.layoutStateMenu.pbLoading.isVisible = true
                binding.layoutStateMenu.tvError.isVisible = false
                binding.rvMenu.isVisible = false
            }, doOnError = {
                binding.layoutStateMenu.root.isVisible = true
                binding.layoutStateMenu.pbLoading.isVisible = false
                binding.layoutStateMenu.tvError.isVisible = true
                binding.layoutStateMenu.tvError.text = it.exception?.message.orEmpty()
                binding.rvMenu.isVisible = false
            }, doOnEmpty = {
                binding.layoutStateMenu.root.isVisible = true
                binding.layoutStateMenu.pbLoading.isVisible = false
                binding.layoutStateMenu.tvError.isVisible = true
                binding.layoutStateMenu.tvError.text = getText(R.string.product_not_found)
                binding.rvMenu.isVisible = false
            })
        }
    }

    private fun observeFoodMenuData() {
        viewModel.menuListLiveData.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                it.payload?.let { send -> foodAdapter.submitData(send) }
            })
        }
    }



    private fun switchLinearGrid() {
        binding.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            (binding.rvMenu.layoutManager as GridLayoutManager).spanCount = if (isChecked) 2 else 1
            foodAdapter.layoutGridLinear =
                if (isChecked) LayoutGridLinear.GRID else LayoutGridLinear.LINEAR
            foodAdapter.refreshList()
        }

    }

    private fun getData() {
        viewModel.getCategories()
        viewModel.getMenus()
    }


}