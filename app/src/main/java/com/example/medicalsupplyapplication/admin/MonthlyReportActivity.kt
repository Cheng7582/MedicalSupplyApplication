package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.database.model.Order
import com.example.medicalsupplyapplication.databinding.ActivityMonthlyReportBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization
import com.example.medicalsupplyapplication.viewModel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MonthlyReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthlyReportBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder>? = null
    private val synchronization = Synchronization()
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        if(orderViewModel.orderList.value == null){
            if(synchronization.isNetworkAvailable(this)){
                orderViewModel.initOnlineOrderList()
            }else{
                lifecycleScope.launch{
                    orderViewModel.initOfflineOrderList(this@MonthlyReportActivity)
                }
            }
        }

        var orderList: MutableList<Order>? = orderViewModel.orderList.value?.toMutableList()
        var ordersByMonth = getOrdersByMonth(orderList)

        layoutManager = LinearLayoutManager(this)
        binding.recycleViewMonthlyReport.layoutManager = layoutManager
        MonthlyReportAdapter.setFragment(this)
        adapter = MonthlyReportAdapter(ordersByMonth)
        binding.recycleViewMonthlyReport.adapter = adapter

        orderViewModel.orderList.observe(this, Observer { newOrder ->
            ordersByMonth = getOrdersByMonth(newOrder)
            (adapter as MonthlyReportAdapter).updateData(ordersByMonth)


            val amount = getGrandTotal(ordersByMonth)
            binding.grandTotal.text = "RM $amount"
        })


        binding.backToSArrow.setOnClickListener {
            val getID = intent.getStringExtra("getID")
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("getID", getID)
            startActivity(intent)
        }
    }

    fun getGrandTotal(ordersByMonth: HashMap<String, HashMap<String, Int>>): Int {
        var amount: Int = 0
        for (item in ordersByMonth.values){
            amount += item["amt"] ?: 0
        }

        return amount
    }

    fun getOrdersByMonth(orderList: MutableList<Order>?): HashMap<String, HashMap<String, Int>>{
        var ordersByMonth = HashMap<String, HashMap<String, Int>>()

        if (orderList != null) {
            for (order in orderList) {
                val timestamp = order.payTime

                var milliseconds: Long = 0
                if(timestamp != null){
                    milliseconds = timestamp.seconds* 1000 + timestamp.nanoseconds/ 1000000
                }

                val sdf = SimpleDateFormat("MM/yyyy")
                val monthYear = sdf.format(Date(milliseconds))

                // Ensure the inner HashMap exists for the current month/year
                val qtyAndAmtHash: HashMap<String, Int> = ordersByMonth.getOrPut(monthYear) { HashMap() }

                // Update the qty and amt values
                qtyAndAmtHash["qty"] = (qtyAndAmtHash["qty"] ?: 0) + order.prodQty
                qtyAndAmtHash["amt"] = (qtyAndAmtHash["amt"] ?: 0) + order.prodQty * order.prodPrice
            }
        }
        return ordersByMonth
    }
}