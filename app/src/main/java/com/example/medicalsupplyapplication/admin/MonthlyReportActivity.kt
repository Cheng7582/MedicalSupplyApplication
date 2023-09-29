package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityMonthlyReportBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.List

class MonthlyReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthlyReportBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var getTimeDate: MutableList<GetTimeDate> = mutableListOf()
        var date = ""
        var conDate = ""
        var i = 0

        var ordersByMonth = HashMap<String, HashMap<String, Int>>()

        Database.db.collection("Order").get().addOnSuccessListener {
/*            for (doc in it) {
                if (date == "") {
                    var timestamp = doc["PayTime"] as com.google.firebase.Timestamp
                    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val netDate = Date(milliseconds)
                    date = sdf.format(netDate).toString()

                    val getTime = GetTimeDate(date)
                    getTimeDate.add(getTime)

                } else {
                    var timestamp = doc["PayTime"] as com.google.firebase.Timestamp
                    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val netDate = Date(milliseconds)
                    conDate = sdf.format(netDate).toString()

                    if (date != conDate) {
                        var timestamp = doc["PayTime"] as com.google.firebase.Timestamp
                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val netDate = Date(milliseconds)
                        date = sdf.format(netDate).toString()

                        val getTime = GetTimeDate(date)
                        getTimeDate.add(getTime)
                    }
                }
            }*/

            for (doc in it) {
                var timestamp = doc["PayTime"] as com.google.firebase.Timestamp
                val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                val sdf = SimpleDateFormat("MM/yyyy")
                val monthYear = sdf.format(Date(milliseconds))

                // Ensure the inner HashMap exists for the current month/year
                val qtyAndAmtHash: HashMap<String, Int> = ordersByMonth.getOrPut(monthYear) { HashMap() }

                // Update the qty and amt values
                qtyAndAmtHash["qty"] = (qtyAndAmtHash["qty"] ?: 0) + doc["ProdQty"].toString().toInt()
                qtyAndAmtHash["amt"] = (qtyAndAmtHash["amt"] ?: 0) + doc["ProdPrice"].toString().toInt()
            }

            layoutManager = LinearLayoutManager(this)

            binding.recycleViewMonthlyReport.layoutManager = layoutManager

            MonthlyReportAdapter.setFragment(this, ordersByMonth)

            adapter = MonthlyReportAdapter(this, ordersByMonth)

            binding.recycleViewMonthlyReport.adapter = adapter
        }

//        binding.grandTotal.text = "RM" + MonthlyReportAdapter.finalTotal

        binding.backToSArrow.setOnClickListener {
            val getID = intent.getStringExtra("getID")
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("getID", getID)
            startActivity(intent)
        }
    }

    fun setVariable(myVariable: Int) {
        binding.grandTotal.text = "RM" + myVariable
    }
}

class GetTimeDate {
    private var date: String

    constructor(Date: String) {
        date = Date
    }

    fun getDate(): String {
        return date
    }
}