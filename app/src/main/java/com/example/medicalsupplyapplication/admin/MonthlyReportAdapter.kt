package com.example.medicalsupplyapplication.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import java.text.SimpleDateFormat
import java.util.Date

class MonthlyReportAdapter(val context: MonthlyReportActivity, val items: HashMap<String, HashMap<String, Int>>) : RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder>() {
    companion object {
        private lateinit var activityActi: MonthlyReportActivity
        var grandTotal: Int = 0

        fun setFragment(activityActi: MonthlyReportActivity, items: HashMap<String, HashMap<String, Int>>) {
            Companion.activityActi = activityActi

            var amt: Int = 0
            for (item in items.values){
                amt += item["amt"] ?: 0
                Companion.grandTotal = amt
            }
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



/*        var qty = 0
        var amount = 0
        var tamo = 0
        var conDate = ""

        holder.date.text = item.getDate()

        Database.db.collection("Order").get().addOnSuccessListener {
            for (doc in it) {
                var timestamp = doc["PayTime"] as com.google.firebase.Timestamp
                val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val netDate = Date(milliseconds)
                conDate = sdf.format(netDate).toString()

                if (item.getDate() == conDate) {
                    amount = Integer.parseInt(
                        doc.get("ProdQty").toString()
                    ) * Integer.parseInt(doc.get("ProdPrice").toString())
                    qty += Integer.parseInt(doc.get("ProdQty").toString())
                    tamo += amount
                }
            }*/

            holder.date.text = monthYearDateKey.toString()
            holder.qty.text = monthQty.toString()
            holder.price.text = "RM" + monthTotal.toString()

//            grandtotal += tamo

            val monthRe: MonthlyReportActivity = context as MonthlyReportActivity
            monthRe.setVariable(Companion.grandTotal)
        }

    override fun getItemCount(): Int {
        return items.size
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