package com.example.medicalsupplyapplication.customer

import android.app.Activity
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
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.databinding.ActivityHomePageBinding

class HomePageActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<HomePageAdapter.ViewHolder>? = null
    private lateinit var HorizontalLayout: LinearLayoutManager
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var imageUrl: ArrayList<String>
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var searchProductList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.visionDisplay.setImageResource(R.drawable.ic_vision)

        val getID = intent.getStringExtra("getID")

        binding.goCart.setOnClickListener {
            val intent = Intent(this,CartActivity::class.java)
            intent.putExtra("getID",getID)
            startActivity(intent)
        }

        binding.searchbar.setQueryHint("Search Product")

        //searchProductList = findViewById(R.id.searchProduct)
        searchProductList = ArrayList()
        Database.db.collection("Product").get().addOnSuccessListener {
            for(doc in it){
                searchProductList.add(doc.get("ProductName").toString())
            }
            listAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                searchProductList
            )

            binding.searchProduct.adapter = listAdapter

            binding.searchbar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.searchProduct.visibility = View.VISIBLE
                    if(searchProductList.contains(query)){
                        listAdapter.filter.filter(query)
                    }else{
                        Log.d(ContentValues.TAG,"No Product Found")
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

            binding.searchProduct.setOnItemClickListener{ parent, view, position, id->
                val element = listAdapter.getItem(position) // The item that was clicked

                Database.db.collection("Product").whereEqualTo("ProductName",element)
                    .get()
                    .addOnSuccessListener {
                        for(doc in it){
                            val intent = Intent(this, ShowProductDetailsActivity::class.java)
                            intent.putExtra("getProdID",doc.get("ProductID").toString())
                            intent.putExtra("getID",getID)
                            startActivity(intent)
                        }
                    }
            }
        }

        var item = 0
        var topSalesList: MutableList<topProduct> = mutableListOf()

        Database.db.collection("Product").get().addOnSuccessListener {
            for(doc in it){
                if(item < 4){
                    val prodID = doc.get("ProductID").toString()
                    val prodName = doc.get("ProductName").toString()
                    val price = Integer.parseInt(doc.get("Price").toString())
                    var newTopList = topProduct(prodID,prodName,price)
                    topSalesList.add(newTopList)
                }
                item++
            }
            binding.searchProduct.visibility = View.GONE

            layoutManager = LinearLayoutManager(this)

            binding.recycleViewProductList.layoutManager = layoutManager

            HomePageAdapter.setFragment(this)

            adapter = HomePageAdapter(this,topSalesList)

            HorizontalLayout = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            binding.recycleViewProductList.layoutManager = HorizontalLayout

            binding.recycleViewProductList.adapter = adapter
        }

        val homepage = HomePageActivity()
        val product = ProductPageActivity()
        val setting = SettingActivity()

        binding.goPPage.setOnClickListener {
            setNewPage(product)
        }

        val c19Category = "COVID-19 Medical Supplies"
        val c20Category = "Durable Medical Equipment & Supplies"
        val c21Category = "Patient Care"
        val c22Category = "Diabetic Products & Services"

        binding.covid19.setOnClickListener {
            val intent = Intent(this,CategoryActivity::class.java)
            intent.putExtra("getID",getID)
            intent.putExtra("getCategory",c19Category)
            startActivity(intent)
        }

        binding.durable.setOnClickListener {
            val intent = Intent(this,CategoryActivity::class.java)
            intent.putExtra("getID",getID)
            intent.putExtra("getCategory",c20Category)
            startActivity(intent)
        }

        binding.diabetic.setOnClickListener {
            val intent = Intent(this,CategoryActivity::class.java)
            intent.putExtra("getID",getID)
            intent.putExtra("getCategory",c22Category)
            startActivity(intent)
        }

        binding.patient.setOnClickListener {
            val intent = Intent(this,CategoryActivity::class.java)
            intent.putExtra("getID",getID)
            intent.putExtra("getCategory",c21Category)
            startActivity(intent)
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> setNewPage(homepage)
                R.id.ic_products -> setNewPage(product)
                R.id.ic_settings -> setNewPage(setting)
            }
            true
        }
    }

    fun toDetail(getProdID: String){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this, ShowProductDetailsActivity::class.java)
        intent.putExtra("getProdID",getProdID)
        intent.putExtra("getID",loginID)
        startActivity(intent)
    }

    private fun setNewPage(activity: Activity){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this,activity::class.java)
        intent.putExtra("getID",loginID)
        startActivity(intent)
        finish()
    }
}

class topProduct{
    private var prodID:String
    private var prodName:String
    private var price:Int

    constructor(){
        prodID = ""
        prodName = ""
        price = 0
    }

    constructor(ProdID:String,ProdName:String,Price:Int){
        prodID = ProdID
        prodName = ProdName
        price = Price
    }

    fun getProdID():String{
        return prodID
    }

    fun getProdName():String{
        return prodName
    }

    fun getPrice():Int{
        return price
    }


}