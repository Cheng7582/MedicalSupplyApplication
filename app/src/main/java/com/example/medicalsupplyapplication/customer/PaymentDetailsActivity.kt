package com.example.medicalsupplyapplication.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityPaymentDetailsBinding
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

class PaymentDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentDetailsBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<PaymentDetailsAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginID = intent.getStringExtra("getID")
        val paymentID = intent.getStringExtra("getPayID")
        var payList: MutableList<paylist> = mutableListOf()
        var tamo = 0
        var date = ""

        Database.db.collection("Customer").whereEqualTo("CustID",loginID)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    binding.username.text = doc.get("Username").toString()
                    binding.userPhone.text = doc.get("Phone").toString()
                    binding.userAdd.text = doc.get("Address").toString()
                }
            }

        Database.db.collection("Order").whereEqualTo("PaymentID",paymentID)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val prodID = doc.get("ProdID").toString()
                    val prodName = doc.get("ProdName").toString()
                    val prodQty = parseInt(doc.get("ProdQty").toString())
                    val prodPrice = parseInt(doc.get("ProdPrice").toString())
                    val subto = prodQty * prodPrice
                    val timestamp = doc["PayTime"] as com.google.firebase.Timestamp
                    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val netDate = Date(milliseconds)
                    date = sdf.format(netDate).toString()
                    tamo += subto
                    var newPayList =
                        paylist(prodID, prodName, prodQty, prodPrice, subto)
                    payList.add(newPayList)
                }

                binding.subtotalamount.text = "RM" + tamo.toString()

                layoutManager = LinearLayoutManager(this)

                binding.recycleViewPaymentDetails.layoutManager = layoutManager

                PaymentDetailsAdapter.setFragment(loginID.toString(),this)

                adapter = PaymentDetailsAdapter(this, payList)

                binding.recycleViewPaymentDetails.adapter = adapter
            }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,OrderHistoryActivity::class.java)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }
    }
}

class paylist{
    private var prodID:String
    private var prodName:String
    private var prodPrice:Int
    private var prodQty:Int
    private var subtotal:Int

    constructor(){
        prodID = ""
        prodName = ""
        prodPrice = 0
        prodQty = 0
        subtotal = 0
    }

    constructor(ProdID:String, ProdName:String, ProdQty:Int, ProdPrice:Int, Subtotal:Int){
        prodID = ProdID
        prodName = ProdName
        prodPrice = ProdPrice
        prodQty = ProdQty
        subtotal = Subtotal
    }

    fun getProdID():String{
        return prodID
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

    fun getSubtotal():Int{
        return subtotal
    }
}