package com.example.medicalsupplyapplication.customer

import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.databinding.ActivityProductPageBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class ProductPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductPageBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter1: RecyclerView.Adapter<C19Adapter.ViewHolder>? = null
    private var adapter2: RecyclerView.Adapter<C20Adapter.ViewHolder>? = null
    private var adapter3: RecyclerView.Adapter<C21Adapter.ViewHolder>? = null
    private var adapter4: RecyclerView.Adapter<C22Adapter.ViewHolder>? = null
    private lateinit var HorizontalLayout: LinearLayoutManager
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var searchProductList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homepage = HomePageActivity()
        val product = ProductPageActivity()
        val setting = SettingActivity()

        binding.myToolbar.setQueryHint("Search Product")

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> setNewPage(homepage)
                R.id.ic_products -> setNewPage(product)
                R.id.ic_settings -> setNewPage(setting)
            }
            true
        }

        val loginID = intent.getStringExtra("getID")

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

            binding.myToolbar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.slidershow.bringToFront()
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
                            intent.putExtra("getID",loginID)
                            startActivity(intent)
                        }
                    }
            }
        }

        var c19List: MutableList<c19list> = mutableListOf()
        var c20List: MutableList<c20list> = mutableListOf()
        var c21List: MutableList<c21list> = mutableListOf()
        var c22List: MutableList<c22list> = mutableListOf()

        val c19Category = "COVID-19 Medical Supplies"
        val c20Category = "Durable Medical Equipment & Supplies"
        val c21Category = "Patient Care"
        val c22Category = "Diabetic Products & Services"

        binding.viewMore1.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("getCategory",c19Category)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }

        binding.viewMore2.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("getCategory",c20Category)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }

        binding.viewMore3.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("getCategory",c21Category)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }

        binding.viewMore4.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("getCategory",c22Category)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }

        Database.db.collection("Product").whereEqualTo("Category",c19Category).get().addOnSuccessListener {
            for(doc in it){
                val prodID = doc.get("ProductID").toString()
                val prodName = doc.get("ProductName").toString()
                var newC19List = c19list(prodID,prodName)
                c19List.add(newC19List)
            }
            layoutManager = LinearLayoutManager(this)

            binding.recycleViewC19.layoutManager = layoutManager

            C19Adapter.setFragment(loginID.toString(),this)

            adapter1 = C19Adapter(this, c19List)

            HorizontalLayout = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recycleViewC19.layoutManager = HorizontalLayout

            // Set adapter on recycler view
            binding.recycleViewC19.adapter = adapter1
        }

        Database.db.collection("Product").whereEqualTo("Category",c20Category).get().addOnSuccessListener {
            for(doc in it){
                val prodID = doc.get("ProductID").toString()
                val prodName = doc.get("ProductName").toString()
                var newC20List = c20list(prodID,prodName)
                c20List.add(newC20List)
            }
            layoutManager = LinearLayoutManager(this)

            binding.recycleViewC20.layoutManager = layoutManager

            C20Adapter.setFragment(loginID.toString(),this)

            adapter2 = C20Adapter(this, c20List)

            HorizontalLayout = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recycleViewC20.layoutManager = HorizontalLayout

            // Set adapter on recycler view
            binding.recycleViewC20.adapter = adapter2
        }

        Database.db.collection("Product").whereEqualTo("Category",c21Category).get().addOnSuccessListener {
            for(doc in it){
                val prodID = doc.get("ProductID").toString()
                val prodName = doc.get("ProductName").toString()
                var newC21List = c21list(prodID,prodName)
                c21List.add(newC21List)
            }
            layoutManager = LinearLayoutManager(this)

            binding.recycleViewC21.layoutManager = layoutManager

            C21Adapter.setFragment(loginID.toString(),this)

            adapter3 = C21Adapter(this, c21List)

            HorizontalLayout = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recycleViewC21.layoutManager = HorizontalLayout

            // Set adapter on recycler view
            binding.recycleViewC21.adapter = adapter3
        }

        Database.db.collection("Product").whereEqualTo("Category",c22Category).get().addOnSuccessListener {
            for(doc in it){
                val prodID = doc.get("ProductID").toString()
                val prodName = doc.get("ProductName").toString()
                var newC22List = c22list(prodID,prodName)
                c22List.add(newC22List)
            }
            layoutManager = LinearLayoutManager(this)

            binding.recycleViewC22.layoutManager = layoutManager

            C22Adapter.setFragment(loginID.toString(),this)

            adapter4 = C22Adapter(this, c22List)

            HorizontalLayout = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recycleViewC22.layoutManager = HorizontalLayout

            // Set adapter on recycler view
            binding.recycleViewC22.adapter = adapter4
        }
    }

    private fun setNewPage(activity: Activity){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this,activity::class.java)
        intent.putExtra("getID",loginID)
        startActivity(intent)
        finish()
    }

    fun toDetail(getProdID: String){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this, ShowProductDetailsActivity::class.java)
        intent.putExtra("getProdID",getProdID)
        intent.putExtra("getID",loginID)
        startActivity(intent)
    }
}

class C19Adapter(val context: ProductPageActivity, val items: List<c19list>): RecyclerView.Adapter<C19Adapter.ViewHolder>()  {

    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : ProductPageActivity

        fun setFragment(getID:String, activityActi: ProductPageActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_page_layout, parent, false)

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
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var prodImg: ImageView
        var name: TextView

        init {
            prodImg = itemView.findViewById(R.id.pgImg)
            name = itemView.findViewById(R.id.pgName)

            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(activityActi,item.getProdID())
            }
        }
    }

    fun toDetail(ActivityAct: ProductPageActivity, getProdID:String){
        ActivityAct.toDetail(getProdID)
    }
}

class C20Adapter(val context: ProductPageActivity, val items: List<c20list>): RecyclerView.Adapter<C20Adapter.ViewHolder>()  {

    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : ProductPageActivity

        fun setFragment(getID:String, activityActi: ProductPageActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_page_layout, parent, false)

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
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var prodImg: ImageView
        var name: TextView

        init {
            prodImg = itemView.findViewById(R.id.pgImg)
            name = itemView.findViewById(R.id.pgName)

            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(activityActi,item.getProdID())
            }
        }
    }

    fun toDetail(ActivityAct: ProductPageActivity, getProdID:String){
        ActivityAct.toDetail(getProdID)
    }
}

class C21Adapter(val context: ProductPageActivity, val items: List<c21list>): RecyclerView.Adapter<C21Adapter.ViewHolder>()  {

    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : ProductPageActivity

        fun setFragment(getID:String, activityActi: ProductPageActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_page_layout, parent, false)

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
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var prodImg: ImageView
        var name: TextView

        init {
            prodImg = itemView.findViewById(R.id.pgImg)
            name = itemView.findViewById(R.id.pgName)

            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(activityActi,item.getProdID())
            }
        }
    }

    fun toDetail(ActivityAct: ProductPageActivity, getProdID:String){
        ActivityAct.toDetail(getProdID)
    }
}

class C22Adapter(val context: ProductPageActivity, val items: List<c22list>): RecyclerView.Adapter<C22Adapter.ViewHolder>()  {

    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : ProductPageActivity

        fun setFragment(getID:String, activityActi: ProductPageActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_page_layout, parent, false)

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
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var prodImg: ImageView
        var name: TextView

        init {
            prodImg = itemView.findViewById(R.id.pgImg)
            name = itemView.findViewById(R.id.pgName)

            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(activityActi,item.getProdID())
            }
        }
    }

    fun toDetail(ActivityAct: ProductPageActivity, getProdID:String){
        ActivityAct.toDetail(getProdID)
    }
}

class c19list{
    private var prodID:String
    private var prodName:String

    constructor(){
        prodID = ""
        prodName = ""
    }

    constructor(ProdID:String,ProdName:String){
        prodID = ProdID
        prodName = ProdName
    }

    fun getProdID():String{
        return prodID
    }

    fun getProdName():String{
        return prodName
    }
}

class c20list{
    private var prodID:String
    private var prodName:String

    constructor(){
        prodID = ""
        prodName = ""
    }

    constructor(ProdID:String,ProdName:String){
        prodID = ProdID
        prodName = ProdName
    }

    fun getProdID():String{
        return prodID
    }

    fun getProdName():String{
        return prodName
    }
}

class c21list{
    private var prodID:String
    private var prodName:String

    constructor(){
        prodID = ""
        prodName = ""
    }

    constructor(ProdID:String,ProdName:String){
        prodID = ProdID
        prodName = ProdName
    }

    fun getProdID():String{
        return prodID
    }

    fun getProdName():String{
        return prodName
    }
}

class c22list{
    private var prodID:String
    private var prodName:String

    constructor(){
        prodID = ""
        prodName = ""
    }

    constructor(ProdID:String,ProdName:String){
        prodID = ProdID
        prodName = ProdName
    }

    fun getProdID():String{
        return prodID
    }

    fun getProdName():String{
        return prodName
    }
}