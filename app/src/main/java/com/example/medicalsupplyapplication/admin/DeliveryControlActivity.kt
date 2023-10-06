package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityDeliveryControlBinding

class DeliveryControlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryControlBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<DeliveryControlAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getLoginID = intent.getStringExtra("getID")

        var deliverycontrol: MutableList<deliveryControl> = mutableListOf()

        Database.db.collection("Delivery")
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    val did = doc.get("DeliveryID").toString()
                    val cid = doc.get("CustID").toString()
                    val sta = doc.get("Status").toString()

                    val setNewData = deliveryControl(did, cid, sta)
                    deliverycontrol.add(setNewData)
                }
                layoutManager = LinearLayoutManager(this)

                binding.recyclerViewDeliveryStatusAdmin.layoutManager = layoutManager

                DeliveryControlAdapter.setFragment(getLoginID.toString(), this)

                adapter = DeliveryControlAdapter(this, deliverycontrol)

                binding.recyclerViewDeliveryStatusAdmin.adapter = adapter
            }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, ManageActivity::class.java)
            intent.putExtra("getID", getLoginID)
            startActivity(intent)
        }
    }

    fun toDetail(deliveryID: String?, index: Int) {
        val getLoginID = intent.getStringExtra("getID")
        val intent = Intent(this, DeliveryDetailsActivity::class.java)
        intent.putExtra("getDeliveryID", deliveryID)
        intent.putExtra("getID", getLoginID)
        startActivity(intent)
    }
}

class deliveryControl {
    private var dID: String
    private var cID: String
    private var status: String

    constructor() {
        dID = ""
        cID = ""
        status = ""
    }

    constructor(ProdID: String, ProdName: String, Qty: String) {
        dID = ProdID
        cID = ProdName
        status = Qty
    }

    fun getDID(): String {
        return dID
    }

    fun getCID(): String {
        return cID
    }

    fun getStatus(): String {
        return status
    }
}
