package com.example.medicalsupplyapplication.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.R

class DeliveryDetailsAdapter(val context: DeliveryDetailsActivity, val items: List<deliveryDetails>) : RecyclerView.Adapter<DeliveryDetailsAdapter.ViewHolder>() {
    companion object {
        private var getLoginID: String = ""
        private lateinit var ActivityActi: DeliveryDetailsActivity
        fun setFragment(getID: String, ActivityActi: DeliveryDetailsActivity) {
            getLoginID = getID
            Companion.ActivityActi = ActivityActi
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeliveryDetailsAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_details_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryDetailsAdapter.ViewHolder, position: Int) {
        val item = items.get(position)
        holder.id.text = item.getProdID()
        holder.name.text = item.getProdName()
        holder.qty.text = item.getQty()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView
        var name: TextView
        var qty: TextView

        init {
            id = itemView.findViewById(R.id.prodID)
            name = itemView.findViewById(R.id.prodName)
            qty = itemView.findViewById(R.id.orderQty)
        }
    }

}