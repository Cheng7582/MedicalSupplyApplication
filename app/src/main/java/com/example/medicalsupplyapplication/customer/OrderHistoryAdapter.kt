package com.example.medicalsupplyapplication.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import java.lang.Integer.parseInt

class OrderHistoryAdapter(val context: OrderHistoryActivity, val items: List<orderlist>): RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : OrderHistoryActivity

        fun setFragment(getID: String, activityActi: OrderHistoryActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.show_payment_list_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        var totalAmount = 0

        val indexNum = position+1
        holder.index.text = indexNum.toString()
        holder.payID.text = item.getPaymentID()
        Database.db.collection("Order").whereEqualTo("PaymentID",item.getPaymentID()).get().addOnSuccessListener {
            for(doc in it){
                val prodPrice = parseInt(doc.get("ProdPrice").toString())
                val prodQty = parseInt(doc.get("ProdQty").toString())
                totalAmount += prodPrice*prodQty
            }
            holder.price.text = "RM" + totalAmount.toString()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var index: TextView
        var payID: TextView
        var price: TextView

        init {
            index = itemView.findViewById(R.id.indexPay)
            payID = itemView.findViewById(R.id.payID)
            price = itemView.findViewById(R.id.totalPrice)

            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(activityActi, getLoginID, item.getPaymentID())
            }
        }
    }

    fun toDetail(ActivityAct: OrderHistoryActivity, getID: String,getPayID:String){
        ActivityAct.toDetail(getID,getPayID)
    }
}