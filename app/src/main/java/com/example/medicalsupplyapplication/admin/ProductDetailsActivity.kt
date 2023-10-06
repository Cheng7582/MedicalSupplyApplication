package com.example.medicalsupplyapplication.admin

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.medicalsupplyapplication.databinding.ActivityProductDetailsBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization
import com.example.medicalsupplyapplication.viewModel.ProductViewModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private val synchronization = Synchronization()
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getProdID = intent.getStringExtra("getProdID")
        val getID = intent.getStringExtra("getID")

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        if(productViewModel.product.value == null){
            if(synchronization.isNetworkAvailable(this)){
                productViewModel.initOnlineProduct(getProdID?:"")
            }
        }

        productViewModel.product.observe(this, Observer { newProduct ->
            binding.showProdID.text = newProduct.productID
            binding.showProdName.text = newProduct.productName
            binding.showPrice.text = "RM " + newProduct.price.toString()
            binding.showProdStock.text = newProduct.stock.toString()
            binding.showBrand.text = newProduct.brand
            binding.showCategory.text = newProduct.category
            binding.showProdDesc.text = newProduct.desc

        })

        if(synchronization.isNetworkAvailable(this)){
            //Get ProductImg
            val storageRef = FirebaseStorage.getInstance().reference.child("Product/$getProdID.jpg")
            val localfile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.productImage.setImageBitmap(bitmap)
            }
        }

        binding.editProdBtn.setOnClickListener {
            val intent = Intent(this, UpdateProductActivity::class.java)
            intent.putExtra("getProdID", getProdID)
            intent.putExtra("getID", getID)
            startActivity(intent)
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("getID", getID)
            startActivity(intent)
        }
    }
}