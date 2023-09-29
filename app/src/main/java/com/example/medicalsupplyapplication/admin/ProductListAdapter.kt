package com.example.medicalsupplyapplication.admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.R

class ProductListAdapter(val context: ProductListActivity, val items: List<prodList>) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    companion object {
        private var getLoginID: String = ""
        private lateinit var ActivityActi: ProductListActivity

        fun setFragment(getID: String, ActivityActi: ProductListActivity) {
            getLoginID = getID
            Companion.ActivityActi = ActivityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_list_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductListAdapter.ViewHolder, position: Int) {
        val indexNum = position + 1
        val item = items.get(position)

        holder.index.text = indexNum.toString()
        holder.id.text = item.getName()
        holder.name.text = item.getStock().toString()

        if (item.getStock() <= 30) {
            holder.name.setTextColor(Color.RED)
        }else{
            holder.name.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var index: TextView
        var id: TextView
        var name: TextView

        init {
            index = itemView.findViewById(R.id.indexProd)
            id = itemView.findViewById(R.id.prodID)
            name = itemView.findViewById(R.id.prodName)

            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(ActivityActi, item.getID(), position)
            }
        }
    }

    fun toDetail(ActivityAct: ProductListActivity, index: String, position: Int) {
        ActivityAct.toDetail(index, position)
    }
}