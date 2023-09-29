package com.example.medicalsupplyapplication.admin

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityProductDetailsBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getProdID = intent.getStringExtra("getProdID")
        val getID = intent.getStringExtra("getID")

        Database.db.collection("Product").whereEqualTo("ProductID", getProdID)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    binding.showProdID.text = doc.get("ProductID").toString()
                    binding.showProdName.text = doc.get("ProductName").toString()
                    binding.showPrice.text = "RM " + doc.get("Price").toString()
                    binding.showProdStock.text = doc.get("Stock").toString()
                    binding.showBrand.text = doc.get("Brand").toString()
                    binding.showCategory.text = doc.get("Category").toString()
                    binding.showProdDesc.text = doc.get("Desc").toString()
                }
            }

        val storageRef = FirebaseStorage.getInstance().reference.child("Product/$getProdID.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.productImage.setImageBitmap(bitmap)
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