package com.example.middleman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

class GoalAdapter(private var goals: List<Goal>,context: Context):
RecyclerView.Adapter<GoalAdapter.GoalViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_item_design,parent,false)
        return GoalViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: GoalViewHolder,
        position: Int
    ) {
        val goal = goals[position]
        holder.categoryTextView.text = goal.category.toString()
        holder.currentAmount.text = currency(goal.currentAmount)
        holder.totalAmount.text = currency(goal.totalAmount)
        val goalPercent = ((goal.currentAmount/goal.totalAmount)*100).roundToInt()
        holder.percentage.text = "$goalPercent %"

        holder.progressBar.max = goal.totalAmount.toInt()
        holder.progressBar.progress = goal.currentAmount.toInt()

        holder.card.setOnClickListener {
            goalId = goal.goalId
            it.findNavController().navigate(R.id.action_goalsFragment_to_editGoal)
        }

    }

    override fun getItemCount(): Int {
        return goals.size
    }

    class GoalViewHolder(goalItemView: View): RecyclerView.ViewHolder(goalItemView) {
        val categoryTextView: TextView = goalItemView.findViewById(R.id.categoryGoalTextView)
        val totalAmount: TextView = goalItemView.findViewById(R.id.totalAmountGoalTextView)
        val currentAmount: TextView = goalItemView.findViewById(R.id.currentAmountGoalTextView)
        val percentage: TextView = goalItemView.findViewById(R.id.percentageGoalTextView)
        val progressBar: ProgressBar = goalItemView.findViewById(R.id.goalProgressBar)
        val card: CardView = goalItemView.findViewById(R.id.goalCard)
    }
    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }
    fun updateItems(newItems:List<Goal>){
        goals = newItems
        notifyDataSetChanged()
    }
}