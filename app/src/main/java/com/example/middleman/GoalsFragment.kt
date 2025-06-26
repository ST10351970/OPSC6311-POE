package com.example.middleman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.middleman.databinding.FragmentGoalsBinding
import java.text.NumberFormat
import java.util.Locale


class GoalsFragment : Fragment() {

    private lateinit var goalsBinding: FragmentGoalsBinding
    private lateinit var goalDatabaseHelper: GaolDatabaseHelper
    private lateinit var goalAdapter: GoalAdapter

    companion object{
        private var goals = mutableListOf<Goal>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        goalsBinding = FragmentGoalsBinding.inflate(layoutInflater,container,false)
        return goalsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalDatabaseHelper = GaolDatabaseHelper(goalsBinding.root.context)



        goalAdapter = GoalAdapter(goalDatabaseHelper.getGoals() ,goalsBinding.root.context)



        goalsBinding.goalsRecyclerView.layoutManager = LinearLayoutManager(goalsBinding.root.context)
        goalsBinding.goalsRecyclerView.adapter = goalAdapter

        // add goal button
        goalsBinding.addGoalButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_goalsFragment_to_createGoalFragment)
        }

        //total goals
        val totalAmount = goalDatabaseHelper.totalGoals()

        goalsBinding.totalGoalsTextView.text = currency(totalAmount)


        goalsBinding.numberOfGoalsTextView.text = goalDatabaseHelper.getGoals().size.toString()


        //go to goal viewer
        goalsBinding.myMiddleManButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_goalsFragment_to_gameFragment)
        }
    }

    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }

    override fun onResume() {
        super.onResume()
        goalAdapter.updateItems(goalDatabaseHelper.getGoals())
    }
}