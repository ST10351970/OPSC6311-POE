package com.example.middleman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.middleman.databinding.FragmentIncomeCategoriesBinding


class IncomeCategoriesFragment : Fragment() {

    private lateinit var incomeCategoriesBinding: FragmentIncomeCategoriesBinding
    private lateinit var incomeDatabase: IncomeDatabase
    private lateinit var incomeCategoryAdapter: IncomeCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        incomeCategoriesBinding = FragmentIncomeCategoriesBinding.inflate(layoutInflater,container,false)
        return incomeCategoriesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incomeDatabase = IncomeDatabase(incomeCategoriesBinding.root.context)
        incomeCategoryAdapter = IncomeCategoryAdapter(incomeDatabase.returnCategories(),incomeCategoriesBinding.root.context)

        incomeCategoriesBinding.expenseCategoriesRecyclerView.layoutManager = LinearLayoutManager(incomeCategoriesBinding.root.context)
        incomeCategoriesBinding.expenseCategoriesRecyclerView.adapter = incomeCategoryAdapter

        incomeCategoriesBinding.newIncomeCategoriesButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_incomeCategoriesFragment_to_createIncomeCategoryFragment)

        }
        incomeCategoriesBinding.expenseCategoriesButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_incomeCategoriesFragment_to_allCategoriesFragment)
        }




    }

    override fun onResume() {
        super.onResume()
        incomeCategoryAdapter.updateItems(incomeDatabase.returnCategories())
    }

}