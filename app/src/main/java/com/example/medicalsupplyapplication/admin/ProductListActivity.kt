package com.example.medicalsupplyapplication.admin


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.database.model.Product
import com.example.medicalsupplyapplication.databinding.ActivityProductListBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization
import com.example.medicalsupplyapplication.viewModel.ProductViewModel


class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ProductListAdapter.ViewHolder>? = null
    private val synchronization = Synchronization()
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val getLoginID = intent.getStringExtra("getID")

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        if (productViewModel.productList.value == null) {
            if (synchronization.isNetworkAvailable(this)) {
                productViewModel.initOnlineProductList()
            }
        }



        var productList: MutableList<Product>? = productViewModel.productList.value?.toMutableList()

        layoutManager = LinearLayoutManager(this)
        binding.recycleViewProduct.layoutManager = layoutManager
        ProductListAdapter.setFragment(getLoginID.toString(), this)
        adapter = ProductListAdapter(this, productList)
        binding.recycleViewProduct.adapter = adapter

        productViewModel.productList.observe(this, Observer { newProduct ->
            (adapter as ProductListAdapter).updateData(newProduct)

        })

        binding.goAddProdBtn.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra("getID", getLoginID)
            startActivity(intent)
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, ManageActivity::class.java)
            intent.putExtra("getID", getLoginID)
            startActivity(intent)
        }
    }

    fun toDetail(index: String, position: Int) {
        val getLoginID = intent.getStringExtra("getID")
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("getProdID", index)
        intent.putExtra("getID", getLoginID)
        intent.putExtra("getIndex", position)
        startActivity(intent)
    }
}