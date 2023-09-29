package com.example.medicalsupplyapplication.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.R

class DeliveryControlAdapter(val context: DeliveryControlActivity, val items: List<deliveryControl>) : RecyclerView.Adapter<DeliveryControlAdapter.ViewHolder>() {
    companion object {
        private var getLoginID: String = ""
        private lateinit var ActivityActi: DeliveryControlActivity

        fun setFragment(getID: String, ActivityActi: DeliveryControlActivity) {
            getLoginID = getID
            Companion.ActivityActi = ActivityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryControlAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_control_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryControlAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

        val indexNum = position + 1
        holder.index.text = indexNum.toString()
        holder.did.text = item.getDID()
        holder.cid.text = item.getCID()
        holder.status.text = item.getStatus()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var index: TextView
        var did: TextView
        var cid: TextView
        var status: TextView

        init {
            index = itemView.findViewById(R.id.indexDeli)
            did = itemView.findViewById(R.id.deliID)
            cid = itemView.findViewById(R.id.deliCustID)
            status = itemView.findViewById(R.id.deliStatus)

            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                toDetail(ActivityActi, position)
            }
        }
    }

    fun toDetail(ActivityAct: DeliveryControlActivity, index: Int) {
        ActivityAct.toDetail(index)
    }
}