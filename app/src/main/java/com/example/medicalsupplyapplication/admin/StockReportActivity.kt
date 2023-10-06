package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.database.model.Product
import com.example.medicalsupplyapplication.databinding.ActivityStockReportBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization
import com.example.medicalsupplyapplication.viewModel.ProductViewModel
import kotlinx.coroutines.launch

class StockReportActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<StockReportAdapter.ViewHolder>? = null
    private lateinit var binding: ActivityStockReportBinding
    private val synchronization = Synchronization()
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        if(productViewModel.productList.value == null){

            if(synchronization.isNetworkAvailable(this)){
                productViewModel.initOnlineProductList()
            }else{
                lifecycleScope.launch {
                    productViewModel.initOfflineProductList(this@StockReportActivity)
                }
            }
        }

        var productList: MutableList<Product>? = productViewModel.productList.value?.toMutableList()

        layoutManager = LinearLayoutManager(this)

        binding.recycleViewStockReport.layoutManager = layoutManager
        StockReportAdapter.setFragment(this)
        adapter = StockReportAdapter(productList)
        binding.recycleViewStockReport.adapter = adapter

        productViewModel.productList.observe(this, Observer { newProducts ->

            (adapter as StockReportAdapter).updateData(newProducts)

        })

        binding.backToSArrow.setOnClickListener {
            val getID = intent.getStringExtra("getID")
            val intent = Intent(this,DashboardActivity::class.java)
            intent.putExtra("getID",getID)
            startActivity(intent)
        }
    }
}