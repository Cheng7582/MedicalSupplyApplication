package com.example.medicalsupplyapplication.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.R


class MonthlyReportAdapter(var items: HashMap<String, HashMap<String, Int>>) : RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder>() {
    companion object {
        private lateinit var activityActi: MonthlyReportActivity

        fun setFragment(activityActi: MonthlyReportActivity) {
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.monthly_report_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val linkedHashMap = items.toSortedMap()

        val monthYearDateKey = linkedHashMap.keys.elementAt(position)
        val monthTotal = linkedHashMap.get(monthYearDateKey)?.get("amt")
        val monthQty = linkedHashMap.get(monthYearDateKey)?.get("qty")

        holder.date.text = monthYearDateKey.toString()
        holder.qty.text = monthQty.toString()
        holder.price.text = "RM" + monthTotal.toString()

        }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: HashMap<String, HashMap<String, Int>>) {
        items.clear()
        items.putAll(newItems)

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView
        var qty: TextView
        var price: TextView

        init {
            date = itemView.findViewById(R.id.DateOfMonth)
            qty = itemView.findViewById(R.id.soldQty)
            price = itemView.findViewById(R.id.daySell)
        }
    }
}