package com.example.medicalsupplyapplication.customer

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class PaymentDetailsAdapter(val context: PaymentDetailsActivity, val items: List<paylist>): RecyclerView.Adapter<PaymentDetailsAdapter.ViewHolder>() {
    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : PaymentDetailsActivity

        fun setFragment(getID: String, activityActi: PaymentDetailsActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_list_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        val id = item.getProdID()
        val storageRef = FirebaseStorage.getInstance().reference.child("Product/$id.jpg")
        val localfile = File.createTempFile("tempImage","jpeg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.prodImg.setImageBitmap(bitmap)
        }

        holder.prodName.text = item.getProdName()
        holder.prodQty.text = " x" + item.getProdQty().toString() + " "
        holder.prodPrice.text = "RM" + item.getProdPrice().toString()
        holder.subtotal.text = "RM" + item.getSubtotal().toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var prodImg: ImageView
        var prodName: TextView
        var prodQty: TextView
        var prodPrice: TextView
        var subtotal: TextView

        init {
            prodImg = itemView.findViewById(R.id.payPImg)
            prodName = itemView.findViewById(R.id.payPName)
            prodQty = itemView.findViewById(R.id.payQty)
            prodPrice = itemView.findViewById(R.id.payPPrice)
            subtotal = itemView.findViewById(R.id.paySubtotal)
        }
    }

}