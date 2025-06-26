package com.example.middleman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.middleman.databinding.FragmentAllCategoriesBinding


class AllCategoriesFragment : Fragment() {

    private lateinit var allCategoriesBinding: FragmentAllCategoriesBinding
    private lateinit var categoriesDatabaseHelper: CategoryDatabaseHelper
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        allCategoriesBinding = FragmentAllCategoriesBinding.inflate(layoutInflater,container,false)
        return allCategoriesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesDatabaseHelper = CategoryDatabaseHelper(allCategoriesBinding.root.context)

        allCategoriesBinding.newCategoryButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_allCategoriesFragment_to_addCategoryFragment)
        }
        allCategoriesBinding.incomeCategoriesButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_allCategoriesFragment_to_incomeCategoriesFragment)
        }

        categoryAdapter = CategoryAdapter(categoriesDatabaseHelper.returnCategories(),allCategoriesBinding.root.context)

        allCategoriesBinding.expenseCategoriesRecyclerView.layoutManager = LinearLayoutManager(allCategoriesBinding.root.context)
        allCategoriesBinding.expenseCategoriesRecyclerView.adapter = categoryAdapter

    }

    override fun onResume() {
        super.onResume()
        categoryAdapter.updateItems(categoriesDatabaseHelper.returnCategories())
    }


}