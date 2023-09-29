package com.example.medicalsupplyapplication.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
//import com.example.medicalsupplyapplication.admin.ProductDetailsActivity
import com.example.medicalsupplyapplication.databinding.ActivityOrderHistoryBinding
import java.lang.Integer.parseInt

class OrderHistoryActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>? = null
    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginID = intent.getStringExtra("getID")

        var orderList: MutableList<orderlist> = mutableListOf()

        var testPayID = ""

        Database.db.collection("Order").whereEqualTo("CustID",loginID).get().addOnSuccessListener {
            for(doc in it) {
                if (testPayID == ""){
                    val orderID = doc.get("OrderID").toString()
                    val prodID = doc.get("ProdID").toString()
                    val custID = doc.get("CustID").toString()
                    testPayID = doc.get("PaymentID").toString()
                    val prodName = doc.get("ProdName").toString()
                    val prodPrice = parseInt(doc.get("ProdPrice").toString())
                    val qty = parseInt(doc.get("ProdQty").toString())

                    var newOrderList =
                        orderlist(orderID, prodID, custID, testPayID, prodName, prodPrice, qty)
                    orderList.add(newOrderList)
                }else if(testPayID != doc.get("PaymentID").toString()){
                    val orderID = doc.get("OrderID").toString()
                    val prodID = doc.get("ProdID").toString()
                    val custID = doc.get("CustID").toString()
                    testPayID = doc.get("PaymentID").toString()
                    val prodName = doc.get("ProdName").toString()
                    val prodPrice = parseInt(doc.get("ProdPrice").toString())
                    val qty = parseInt(doc.get("ProdQty").toString())

                    var newOrderList =
                        orderlist(orderID, prodID, custID, testPayID, prodName, prodPrice, qty)
                    orderList.add(newOrderList)
                }
            }

            layoutManager = LinearLayoutManager(this)

            binding.recycleViewPayment.layoutManager = layoutManager

            OrderHistoryAdapter.setFragment(loginID.toString(),this)

            adapter = OrderHistoryAdapter(this, orderList)

            binding.recycleViewPayment.adapter = adapter
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }
    }

    fun toDetail(getID: String, getPayID: String){
        val intent = Intent(this, PaymentDetailsActivity::class.java)
        intent.putExtra("getID",getID)
        intent.putExtra("getPayID",getPayID)
        startActivity(intent)
    }
}

class orderlist{
    private var orderID:String
    private var prodID:String
    private var custID:String
    private var paymentID:String
    private var prodName:String
    private var prodPrice:Int
    private var prodQty:Int

    constructor(){
        orderID = ""
        prodID = ""
        custID = ""
        paymentID = ""
        prodName = ""
        prodPrice = 0
        prodQty = 0
    }

    constructor(OrderID:String, ProdID:String, CustID:String, PaymentID:String, ProdName:String, ProdPrice:Int, ProdQty:Int){
        orderID = OrderID
        prodID = ProdID
        custID = CustID
        paymentID = PaymentID
        prodName = ProdName
        prodPrice = ProdPrice
        prodQty = ProdQty
    }

    fun getOrderID():String{
        return orderID
    }

    fun getProdID():String{
        return prodID
    }

    fun getCustID():String{
        return custID
    }

    fun getPaymentID():String{
        return paymentID
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