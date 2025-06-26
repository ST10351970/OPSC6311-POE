package com.example.middleman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.text.NumberFormat
import java.util.Locale

class BadgeAdapter(private var badges:List<Goal>,context: Context):
RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BadgeViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.goal_card,parent,false)
        return BadgeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BadgeViewHolder,
        position: Int
    ) {
        var badge = badges[position]
        holder.badgeTitle.text = "Goal Achieved: "+ badge.category.toString()
        holder.amountText.text = currency(badge.totalAmount)
    }

    override fun getItemCount(): Int {
        return badges.size
    }

    class BadgeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val badgeTitle: TextView = itemView.findViewById(R.id.goalAchievedTitleTextView)
        val amountText: TextView = itemView.findViewById(R.id.goalAchievedAmount)
    }
    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }
    fun autoUpdateBadges(newBadges:List<Goal>){
        badges = newBadges
        notifyDataSetChanged()
    }
}