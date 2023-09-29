package com.example.medicalsupplyapplication.admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R

class StockReportAdapter : RecyclerView.Adapter<StockReportAdapter.ViewHolder>() {
    companion object {
        private lateinit var ActivityActi: StockReportActivity

        fun setFragment(ActivityActi: StockReportActivity) {
            Companion.ActivityActi = ActivityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockReportAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_report_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockReportAdapter.ViewHolder, position: Int) {
        holder.id.text = Database.products[position].getID()
        holder.name.text = Database.products[position].getName()
        holder.qty.text = Database.products[position].getStock().toString()
        if (Database.products[position].getStock() > 30) {
            holder.status.text = "Available"
            holder.status.setTextColor(Color.GREEN)

        } else {
            holder.status.text = "Low Quantity"
            holder.status.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return Database.products.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView
        var name: TextView
        var qty: TextView
        var status: TextView

        init {
            id = itemView.findViewById(R.id.ProdID)
            name = itemView.findViewById(R.id.ProdName)
            qty = itemView.findViewById(R.id.quanty)
            status = itemView.findViewById(R.id.status)
        }
    }

}