package com.example.medicalsupplyapplication.customer

import android.R
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityCategoryBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Integer.parseInt

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var searchProductList: ArrayList<String>
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CategoryAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchbar.setQueryHint("Search Product")

        val getID = intent.getStringExtra("getID")
        val getCate = intent.getStringExtra("getCategory")

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,ProductPageActivity::class.java)
            intent.putExtra("getID",getID)
            startActivity(intent)
        }

        binding.categoryName.text = " "+ getCate.toString()

        searchProductList = ArrayList()
        Database.db.collection("Product").whereEqualTo("Category",getCate).get().addOnSuccessListener {
            for(doc in it){
                searchProductList.add(doc.get("ProductName").toString())
            }
            listAdapter = ArrayAdapter<String>(
                this,
                R.layout.simple_list_item_1,
                searchProductList
            )

            binding.searchProduct.adapter = listAdapter

            binding.searchbar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.searchProduct.bringToFront()
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

        var cateList: MutableList<cateProduct> = mutableListOf()

        Database.db.collection("Product").whereEqualTo("Category",getCate)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val prodID = doc.get("ProductID").toString()
                    val prodName = doc.get("ProductName").toString()
                    val price = parseInt(doc.get("Price").toString())
                    val newData = cateProduct(prodID,prodName,price)
                    cateList.add(newData)
                }
                layoutManager = LinearLayoutManager(this)

                binding.recyclerViewCategory.layoutManager = layoutManager

                CategoryAdapter.setFragment(getID.toString(),this)

                binding.recyclerViewCategory.setLayoutManager(GridLayoutManager(this, 2))

                adapter = CategoryAdapter(this, cateList)

                binding.recyclerViewCategory.adapter = adapter
            }
    }
    fun toDetail(getProdID: String){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this, ShowProductDetailsActivity::class.java)
        intent.putExtra("getProdID",getProdID)
        intent.putExtra("getID",loginID)
        startActivity(intent)
    }
}

class CategoryAdapter(val context: CategoryActivity, val items: List<cateProduct>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>()  {

    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : CategoryActivity

        fun setFragment(getID:String, activityActi: CategoryActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.medicalsupplyapplication.R.layout.category_product_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        val prodID = item.getProdID()
        Log.e("checkProdID", item.getProdName())
        val storageRef = FirebaseStorage.getInstance().reference.child("Product/$prodID.jpg")
        val localfile = File.createTempFile("tempImage", "jpeg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.prodImg.setImageBitmap(bitmap)
        }
        holder.name.text = item.getProdName()
        holder.price.text = "RM" + item.getPrice()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var prodImg: ImageView
        var name: TextView
        var price: TextView

        init {
            prodImg = itemView.findViewById(com.example.medicalsupplyapplication.R.id.showImage)
            name = itemView.findViewById(com.example.medicalsupplyapplication.R.id.prodNameC)
            price = itemView.findViewById(com.example.medicalsupplyapplication.R.id.price)

            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(activityActi,item.getProdID())
            }
        }
    }

    fun toDetail(ActivityAct: CategoryActivity, getProdID:String){
        ActivityAct.toDetail(getProdID)
    }
}

class cateProduct{
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