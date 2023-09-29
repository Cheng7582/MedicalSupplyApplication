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

class HomePageAdapter(val context: HomePageActivity, val items: List<topProduct>): RecyclerView.Adapter<HomePageAdapter.ViewHolder>() {
    companion object{
        private lateinit var activityActi : HomePageActivity
        fun setFragment(activityActi: HomePageActivity){
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.show_product_list_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        val id = item.getProdID()
        val storageRef = FirebaseStorage.getInstance().reference.child("Product/$id.jpg")
        val localfile = File.createTempFile("tempImage", "jpeg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.fdImg.setImageBitmap(bitmap)
        }
        holder.name.text = item.getProdName()
        holder.price.text = "RM " + item.getPrice()

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var fdImg: ImageView
        var name: TextView
        var price: TextView

        init {
            fdImg = itemView.findViewById(R.id.getProdImg)
            name = itemView.findViewById(R.id.showProdNameInList)
            price = itemView.findViewById(R.id.showProdPriceInList)

            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(activityActi,item.getProdID())
            }
        }
    }

    fun toDetail(ActivityAct: HomePageActivity, getProdID:String){
        ActivityAct.toDetail(getProdID)
    }
}