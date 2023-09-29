package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityDeliveryDetailsBinding
import com.google.firebase.firestore.FieldValue

class DeliveryDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryDetailsBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<DeliveryDetailsAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getID = intent.getStringExtra("getID")
        val getIndex = intent.getIntExtra("getIndex", 0)
        val deliveryID = Database.deliverys[getIndex].getDID()

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, DeliveryControlActivity::class.java)
            intent.putExtra("getID", getID)
            startActivity(intent)
        }

        var deliverydetail: MutableList<deliveryDetails> = mutableListOf()

        Database.db.collection("Delivery").whereEqualTo("DeliveryID", deliveryID)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    binding.deID.text = deliveryID
                    binding.dePayID.text = doc.get("PayID").toString()
                    binding.deStatus.text = doc.get("Status").toString()
                    for (cust in Database.customers) {
                        if (cust.getID() == doc.get("CustID").toString()) {
                            binding.deUsername.text = cust.getName()
                            binding.dephone.text = cust.getPhone()
                            binding.deAdd.text = cust.getAddress()
                        }
                    }
                }
                Database.db.collection("Order")
                    .whereEqualTo("PaymentID", binding.dePayID.text.toString())
                    .get()
                    .addOnSuccessListener {
                        for (doc in it) {
                            val prodID = doc.get("ProdID").toString()
                            val prodName = doc.get("ProdName").toString()
                            val qty = doc.get("ProdQty").toString()
                            val newData = deliveryDetails(prodID, prodName, qty)
                            deliverydetail.add(newData)
                        }
                        layoutManager = LinearLayoutManager(this)

                        binding.recycleViewDeliveryItem.layoutManager = layoutManager

                        DeliveryDetailsAdapter.setFragment(getID.toString(), this)

                        adapter = DeliveryDetailsAdapter(this, deliverydetail)

                        binding.recycleViewDeliveryItem.adapter = adapter
                    }
            }

        if (binding.deStatus.text.toString() == "Delivered") {
            binding.updateDeliStatus.visibility = View.GONE
        }

        binding.updateDeliStatus.setOnClickListener {
            var idNum = 0

            Database.db.collection("DeliveryStatus")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snapshot = task.result
                        idNum = snapshot.count() + 1
                    }
                    Log.e("Check IDNum", idNum.toString())
                    val newID = "DS" + String.format("%06d", idNum)

                    if (binding.deStatus.text == "Preparing") {
                        val addNewData = hashMapOf(
                            "DsID" to newID,
                            "DeliveryID" to binding.deID.text.toString(),
                            "Desc" to "Parcel hand over to logistics partner",
                            "Date" to FieldValue.serverTimestamp()
                        )

                        Database.db.collection("DeliveryStatus").document(newID)
                            .set(addNewData)

                        Database.db.collection("Delivery").document(binding.deID.text.toString())
                            .update(
                                "Status", "Pick up"
                            ).addOnSuccessListener {
                                val intent = Intent(this, DeliveryDetailsActivity::class.java)
                                intent.putExtra("getID", getID)
                                intent.putExtra("getIndex", getIndex)
                                startActivity(intent)
                                finish()
                            }
                    } else if (binding.deStatus.text == "Pick up") {
                        val addNewData = hashMapOf(
                            "DsID" to newID,
                            "DeliveryID" to binding.deID.text.toString(),
                            "Desc" to "Parcel has arrived at sorting facility.",
                            "Date" to FieldValue.serverTimestamp()
                        )

                        Database.db.collection("DeliveryStatus").document(newID)
                            .set(addNewData)

                        Database.db.collection("Delivery").document(binding.deID.text.toString())
                            .update(
                                "Status", "In transit"
                            ).addOnSuccessListener {
                                val intent = Intent(this, DeliveryDetailsActivity::class.java)
                                intent.putExtra("getID", getID)
                                intent.putExtra("getIndex", getIndex)
                                startActivity(intent)
                                finish()
                            }
                    } else if (binding.deStatus.text == "In transit") {
                        val addNewData = hashMapOf(
                            "DsID" to newID,
                            "DeliveryID" to binding.deID.text.toString(),
                            "Desc" to "Parcel is out of delivery today.",
                            "Date" to FieldValue.serverTimestamp()
                        )

                        Database.db.collection("DeliveryStatus").document(newID)
                            .set(addNewData)

                        Database.db.collection("Delivery").document(binding.deID.text.toString())
                            .update(
                                "Status", "Out of delivery"
                            ).addOnSuccessListener {
                                val intent = Intent(this, DeliveryDetailsActivity::class.java)
                                intent.putExtra("getID", getID)
                                intent.putExtra("getIndex", getIndex)
                                startActivity(intent)
                                finish()
                            }
                    } else if (binding.deStatus.text == "Out of delivery") {
                        val addNewData = hashMapOf(
                            "DsID" to newID,
                            "DeliveryID" to binding.deID.text.toString(),
                            "Desc" to "Parcel has been delivered.",
                            "Date" to FieldValue.serverTimestamp()
                        )

                        Database.db.collection("DeliveryStatus").document(newID)
                            .set(addNewData)

                        Database.db.collection("Delivery").document(binding.deID.text.toString())
                            .update(
                                "Status", "Delivered"
                            ).addOnSuccessListener {
                                val intent = Intent(this, DeliveryDetailsActivity::class.java)
                                intent.putExtra("getID", getID)
                                intent.putExtra("getIndex", getIndex)
                                startActivity(intent)
                                finish()
                            }
                    } else if (binding.deStatus.text == "Delivered") {
                        Toast.makeText(this, "This order already delivered", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

    }
}

class deliveryDetails {
    private var prodID: String
    private var prodName: String
    private var qty: String

    constructor() {
        prodID = ""
        prodName = ""
        qty = ""
    }

    constructor(ProdID: String, ProdName: String, Qty: String) {
        prodID = ProdID
        prodName = ProdName
        qty = Qty
    }

    fun getProdID(): String {
        return prodID
    }

    fun getProdName(): String {
        return prodName
    }

    fun getQty(): String {
        return qty
    }
}

