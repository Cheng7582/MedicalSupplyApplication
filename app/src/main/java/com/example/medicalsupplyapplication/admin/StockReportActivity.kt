package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.databinding.ActivityStockReportBinding

class StockReportActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<StockReportAdapter.ViewHolder>? = null
    private lateinit var binding: ActivityStockReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)

        binding.recycleViewStockReport.layoutManager = layoutManager

        StockReportAdapter.setFragment(this)

        adapter = StockReportAdapter()

        binding.recycleViewStockReport.adapter = adapter

        binding.backToSArrow.setOnClickListener {
            val getID = intent.getStringExtra("getID")
            val intent = Intent(this,DashboardActivity::class.java)
            intent.putExtra("getID",getID)
            startActivity(intent)
        }
    }
}