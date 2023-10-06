package com.example.medicalsupplyapplication.admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.database.model.Product

class StockReportAdapter(var products: MutableList<Product>?) : RecyclerView.Adapter<StockReportAdapter.ViewHolder>() {
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
        holder.id.text = products?.get(position)?.productID
        holder.name.text = products?.get(position)?.productName
        holder.qty.text = products?.get(position)?.stock.toString()
        if (products?.get(position)?.stock?:0 > 30) {
            holder.status.text = "Available"
            holder.status.setTextColor(Color.GREEN)

        } else {
            holder.status.text = "Low Quantity"
            holder.status.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return products?.size?:0
    }

    fun updateData(newProducts: MutableList<Product>?) {
        products?.clear()
        products = newProducts

        notifyDataSetChanged()
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