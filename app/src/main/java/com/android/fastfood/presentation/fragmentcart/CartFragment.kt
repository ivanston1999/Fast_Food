package com.android.fastfood.presentation.fragmentcart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.fastfood.R
import com.android.fastfood.databinding.FragmentCartBinding


class CartFragment : Fragment() {


    private lateinit var binding: FragmentCartBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCartBinding.inflate(inflater,container, false)
        return binding.root
    }


}