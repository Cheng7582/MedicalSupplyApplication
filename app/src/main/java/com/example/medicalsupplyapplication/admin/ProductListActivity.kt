package com.example.medicalsupplyapplication.admin

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityProductListBinding
import java.lang.Integer.parseInt

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ProductListAdapter.ViewHolder>? = null
    var ProdList: MutableList<prodList> = mutableListOf()
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var searchProductList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getLoginID = intent.getStringExtra("getID")

        binding.searchbar.setQueryHint("Search Product")

        //searchProductList = findViewById(R.id.searchProduct)
        searchProductList = ArrayList()
        Database.db.collection("Product").get().addOnSuccessListener {
            for (doc in it) {
                searchProductList.add(doc.get("ProductName").toString())
            }
            listAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                searchProductList
            )

            binding.searchProduct.adapter = listAdapter

            binding.searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.searchProduct.visibility = View.VISIBLE
                    binding.searchProduct.bringToFront()
                    if (searchProductList.contains(query)) {
                        listAdapter.filter.filter(query)
                    } else {
                        Log.d(ContentValues.TAG, "No Product Found")
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // if query text is change in that case we
                    // are filtering our adapter with
                    // new text on below line.
                    listAdapter.filter.filter(newText)
                    return false
                }
            })

            binding.searchProduct.setOnItemClickListener { parent, view, position, id ->
                val element = listAdapter.getItem(position) // The item that was clicked

                Database.db.collection("Product").whereEqualTo("ProductName", element)
                    .get()
                    .addOnSuccessListener {
                        for (doc in it) {
                            val intent = Intent(this, ProductDetailsActivity::class.java)
                            intent.putExtra("getProdID", doc.get("ProductID").toString())
                            intent.putExtra("getID", getLoginID)
                            startActivity(intent)
                        }
                    }
            }
        }

        Database.db.collection("Product")
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    val prodID = doc.get(("ProductID")).toString()
                    val prodName = doc.get("ProductName").toString()
                    val stock = parseInt(doc.get("Stock").toString())
                    val addNewData = prodList(prodID, prodName, stock)
                    ProdList.add(addNewData)
                }
                layoutManager = LinearLayoutManager(this)

                binding.recycleViewProduct.layoutManager = layoutManager

                ProductListAdapter.setFragment(getLoginID.toString(), this)

                adapter = ProductListAdapter(this, ProdList)

                binding.recycleViewProduct.adapter = adapter
            }

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

class prodList {
    private var pID: String
    private var stock: Int
    private var pname: String

    constructor(PID: String, PName: String, Stock: Int) {
        pID = PID
        pname = PName
        stock = Stock
    }

    fun getID(): String {
        return pID
    }

    fun getName(): String {
        return pname
    }

    fun getStock(): Int {
        return stock
    }
}