package com.example.medicalsupplyapplication.customer

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.database.model.Cart
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Integer.parseInt

class CartAdapter(val context: CartActivity, val items: MutableList<Cart>): RecyclerView.Adapter<CartAdapter.ViewHolder>()  {

    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : CartActivity

        fun setFragment(getID:String, activityActi: CartActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    var setQty: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_list_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        val prodID = item.prodID
        Log.e("checkProdID", prodID)
        val storageRef = FirebaseStorage.getInstance().reference.child("Product/$prodID.jpg")
        val localfile = File.createTempFile("tempImage", "jpeg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.prodImg.setImageBitmap(bitmap)
        }
        holder.name.text = item.prodName
        holder.price.text = "RM " + item.prodPrice
        holder.qty.text = item.prodQty.toString()

        holder.addBtn.setOnClickListener {
            Database.db.collection("Product").whereEqualTo("ProductName",item.prodName).get().addOnSuccessListener {
                for(doc in it){
                    if(setQty < parseInt(doc.get("Stock").toString())){
                        setQty = item.prodQty
                        setQty += 1
                        Database.db.collection("Cart").document(item.cartID)
                            .update("Qty", setQty)
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "DocumentSnapshot successfully!")
                                holder.qty.text = setQty.toString()
                                refresh(activityActi,getLoginID)
                            }
                            .addOnFailureListener {
                                Log.d(ContentValues.TAG,"Error update data",it)
                            }
                    }else{
                        Toast.makeText(activityActi,"Inventory is empty.",Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

        holder.minusBtn.setOnClickListener {
            setQty = item.prodQty
            setQty -= 1
            Database.db.collection("Cart").document(item.cartID)
                .update("Qty", setQty)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully!")
                    holder.qty.text = setQty.toString()
                    refresh(activityActi,getLoginID)
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error update data",it)
                }
        }

        holder.deleteBtn.setOnClickListener{
            Database.db.collection("Cart").document(item.cartID)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully!")
                    val getID = getLoginID
                    refresh(activityActi,getID)
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error delete data",it)
                }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var prodImg: ImageView
        var name: TextView
        var price: TextView
        var qty: TextView
        lateinit var minusBtn: Button
        lateinit var addBtn: Button
        lateinit var deleteBtn: ImageButton

        init {
            prodImg = itemView.findViewById(R.id.getCartProdImg)
            name = itemView.findViewById(R.id.showProdNameInCart)
            price = itemView.findViewById(R.id.showProdPriceInCart)
            qty = itemView.findViewById(R.id.prodQty)
            minusBtn = itemView.findViewById(R.id.minusOne)
            addBtn = itemView.findViewById(R.id.addOne)
            deleteBtn = itemView.findViewById(R.id.deleteButton)
        }
    }

    fun refresh(ActivityAct: CartActivity, getID: String){
        ActivityAct.refresh(getID)
    }
}