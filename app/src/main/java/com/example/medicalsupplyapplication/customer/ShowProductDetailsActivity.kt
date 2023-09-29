package com.example.medicalsupplyapplication.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.Login
import com.example.medicalsupplyapplication.databinding.ActivityShowProductDetailsBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Integer.parseInt

class ShowProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getProdID = intent.getStringExtra("getProdID")
        val getLoginID = intent.getStringExtra("getID")

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,HomePageActivity::class.java)
            intent.putExtra("getID",getLoginID)
            startActivity(intent)
        }

        Database.db.collection("Product").whereEqualTo("ProductID",getProdID)
            .get()
            .addOnSuccessListener {
                for(doc in it) {
                    binding.showProductName.text = doc.get("ProductName").toString()
                    binding.showProductPrice.text =
                        "RM " + doc.get("Price").toString()
                    binding.showProductDesc.text = doc.get("Desc").toString()
                    binding.showProductStock.text =
                        doc.get("Stock").toString()
                    binding.showBrand.text = doc.get("Brand").toString()
                    binding.showCategory.text = doc.get("Category").toString()

                    val prodName = binding.showProductName.text.toString()
                    val prodPrice = parseInt(doc.get("Price").toString())

                    val prodID = doc.get("ProductID").toString()

                    val storageRef =
                        FirebaseStorage.getInstance().reference.child("Product/$prodID.jpg")
                    val localfile = File.createTempFile("tempImage", "jpg")

                    storageRef.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        binding.showProductImg.setImageBitmap(bitmap)
                    }

                    binding.backToSArrow.setOnClickListener {
                        val intent = Intent(this,ProductPageActivity::class.java)
                        intent.putExtra("getID",getLoginID)
                        startActivity(intent)
                    }

                    var boolean = true

                    binding.addToCart.setOnClickListener {
                        if(getLoginID == "" || getLoginID == null){
                            Toast.makeText(this, "Please Login First.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                        }else if(doc.get("Stock") == 0){
                            Toast.makeText(this, "Very sorry, the product is out of stock now.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ShowProductDetailsActivity::class.java)
                            startActivity(intent)
                        }else{
                            for(it in Database.carts){
                                if(getLoginID == it.getCustID() && prodID == it.getProdID()){
                                    val cartID = it.getCartID()
                                    val newQty = it.getProdQty() + 1
                                    Database.db.collection("Cart").document(cartID)
                                        .update("Qty", newQty)
                                        .addOnSuccessListener {
                                            Log.d(ContentValues.TAG, "DocumentSnapshot successfully!")
                                        }
                                        .addOnFailureListener {
                                            Log.d(ContentValues.TAG,"Error update data",it)
                                        }
                                    boolean = false

                                    val intent = Intent(this, CartActivity::class.java)
                                    intent.putExtra("getID", getLoginID)
                                    startActivity(intent)
                                }
                            }

                            if(boolean) {
                                var getIDNum = 0

                                for(it in Database.carts){
                                    getIDNum = it.getIDNum()
                                }

                                val newID: Int = getIDNum + 1
                                val cartID = "SC000" + newID

                                Database.addCart(
                                    cartID,
                                    prodID,
                                    getLoginID.toString(),
                                    prodName,
                                    prodPrice,
                                    1,
                                    newID
                                )

                                val intent = Intent(this, CartActivity::class.java)
                                intent.putExtra("getID", getLoginID)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
    }
}