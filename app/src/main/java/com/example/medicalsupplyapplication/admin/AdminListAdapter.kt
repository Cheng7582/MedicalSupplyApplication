package com.example.medicalsupplyapplication.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R

class AdminListAdapter : RecyclerView.Adapter<AdminListAdapter.ViewHolder>() {
    companion object {
        private var getLoginID: String = ""
        private lateinit var ActivityActi: AdminListActivity
        fun setFragment(getID: String, ActivityActi: AdminListActivity) {
            getLoginID = getID
            Companion.ActivityActi = ActivityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminListAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_list_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminListAdapter.ViewHolder, position: Int) {
        val indexNum = position + 1
        holder.index.text = indexNum.toString()
        holder.id.text = Database.admins[position].getID()
        holder.name.text = Database.admins[position].getName()

    }

    override fun getItemCount(): Int {
        return Database.admins.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var index: TextView
        var id: TextView
        var name: TextView

        init {
            index = itemView.findViewById(R.id.indexAdmin)
            id = itemView.findViewById(R.id.adminID)
            name = itemView.findViewById(R.id.adminName)

            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                toDetail(ActivityActi, position)
            }
        }
    }

    fun toDetail(ActivityAct: AdminListActivity, index: Int) {
        ActivityAct.toDetail(index)
    }
}