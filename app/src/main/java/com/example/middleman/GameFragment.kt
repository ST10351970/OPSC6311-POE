package com.example.middleman

import android.icu.number.Precision.currency
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.middleman.databinding.FragmentGameBinding
import java.text.NumberFormat
import java.util.Locale


class GameFragment : Fragment() {

    private lateinit var gameBinding: FragmentGameBinding
    private lateinit var goalDatabaseHelper: GaolDatabaseHelper
    private lateinit var badgeAdapter: BadgeAdapter

    companion object{
        private var badges = mutableListOf<Goal>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gameBinding = FragmentGameBinding.inflate(layoutInflater,container,false)
        return gameBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalDatabaseHelper = GaolDatabaseHelper(gameBinding.root.context)

        //retrieving all completed goals and converting into badges



        badgeAdapter = BadgeAdapter(goalDatabaseHelper.getCompletedGoals(),gameBinding.root.context)

        gameBinding.numberOfBadges.text = goalDatabaseHelper.getCompletedGoals().size.toString()

        //getting the total amount of the goals achieved
        //empty image logic
        if(goalDatabaseHelper.getCompletedGoals().isEmpty()){
            gameBinding.emptyImage.visibility = View.VISIBLE
        }
        else{
            gameBinding.emptyImage.visibility = View.INVISIBLE
        }

        //assigning the total amount achieved
        gameBinding.totalBadgeAmount.text = currency(goalDatabaseHelper.totalCompletedGoals())

        gameBinding.badgesRecyclerView.layoutManager = LinearLayoutManager(gameBinding.root.context)
        gameBinding.badgesRecyclerView.adapter = badgeAdapter

        //back button navigation
        gameBinding.backButton.setOnClickListener {
             it.findNavController().navigate(R.id.action_gameFragment_to_goalsFragment)
        }

    }
    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }

    override fun onResume() {
        super.onResume()
        badgeAdapter.autoUpdateBadges(goalDatabaseHelper.getCompletedGoals())
    }

}