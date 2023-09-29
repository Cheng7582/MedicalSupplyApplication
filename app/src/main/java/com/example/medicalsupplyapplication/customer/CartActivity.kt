package com.example.medicalsupplyapplication.customer

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityCartBinding
import java.lang.Integer.parseInt


class CartActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CartAdapter.ViewHolder>? = null
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val setting = SettingActivity()
        val product = ProductPageActivity()

        binding.buyOtherBtn.setTextColor(Color.BLUE)

        binding.backToSArrow.setOnClickListener {
            setNewPage(setting)
        }

        binding.continueShop.setOnClickListener {
            setNewPage(product)
        }



        val loginID = intent.getStringExtra("getID")
        var tAmount = 0

        var cartList: MutableList<cartlist> = mutableListOf()

        Database.db.collection("Cart").whereEqualTo("CustID",loginID).get().addOnSuccessListener {
            for(doc in it){
                val cartID = doc.get("CartID").toString()
                val prodID = doc.get("ProdID").toString()
                val custID = doc.get("CustID").toString()
                val prodName = doc.get("ProdName").toString()
                val prodPrice = parseInt(doc.get("ProdPrice").toString())
                val qty = parseInt(doc.get("Qty").toString())
                var newCartList = cartlist(cartID,prodID,custID,prodName,prodPrice,qty)
                tAmount += (prodPrice*qty)
                cartList.add(newCartList)
            }
            binding.showAmount.text = "RM" + tAmount

            layoutManager = LinearLayoutManager(this)

            binding.recycleViewCart.layoutManager = layoutManager

            CartAdapter.setFragment(loginID.toString(),this)

            adapter = CartAdapter(this, cartList)

            binding.recycleViewCart.adapter = adapter

            if(cartList.isEmpty()){
                binding.emptyMessage.visibility = View.VISIBLE
            }else{
                binding.emptyMessage.visibility = View.GONE
            }
        }

        var paymentAmount = 0
        var custEmail = ""
        var custContact = ""

        binding.topayBtn.setOnClickListener {
            Database.db.collection("Cart").whereEqualTo("CustID",loginID).get().addOnSuccessListener {
                for (doc in it) {
                    val prodPrice = parseInt(doc.get("ProdPrice").toString())
                    val qty = parseInt(doc.get("Qty").toString())
                    paymentAmount += (prodPrice * qty)
                }
                Database.db.collection("Customer").whereEqualTo("CustID",loginID).get().addOnSuccessListener {
                    for(doc in it){
                        custEmail = doc.get("Email") as String
                        custContact = doc.get("Phone") as String
                    }
//                    startPayment(custEmail,custContact,paymentAmount)
                    startPayment()
                }
            }
        }
    }

    private fun setNewPage(activity: Activity){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this,activity::class.java)
        intent.putExtra("getID",loginID)
        startActivity(intent)
        finish()
    }

    fun refresh(getID: String){
        val intent = Intent(this, CartActivity::class.java)
        intent.putExtra("getID",getID)
        startActivity(intent)
    }

    private fun startPayment(){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this,PaymentActivity::class.java)
        intent.putExtra("getID",loginID)
        startActivity(intent)
        finish()
    }
}

class cartlist{
    private var cartID:String
    private var prodID:String
    private var custID:String
    private var prodName:String
    private var prodPrice:Int
    private var prodQty:Int

    constructor(){
        cartID = ""
        prodID = ""
        custID = ""
        prodName = ""
        prodPrice = 0
        prodQty = 0
    }

    constructor(CartID:String, ProdID:String, CustID:String, ProdName:String, ProdPrice:Int, ProdQty:Int){
        cartID = CartID
        prodID = ProdID
        custID = CustID
        prodName = ProdName
        prodPrice = ProdPrice
        prodQty = ProdQty
    }

    fun getCartID():String{
        return cartID
    }

    fun getProdID():String{
        return prodID
    }

    fun getCustID():String{
        return custID
    }

    fun getProdName():String{
        return prodName
    }

    fun getProdPrice():Int{
        return prodPrice
    }

    fun getProdQty():Int{
        return prodQty
    }
}