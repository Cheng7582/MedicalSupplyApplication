package com.example.medicalsupplyapplication.admin

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.databinding.ActivityAddProductBinding
import com.google.firebase.storage.FirebaseStorage


class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.takeImageBtn.isEnabled = false

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 111)
        } else {
            binding.takeImageBtn.isEnabled = true
        }

        binding.takeImageBtn.setOnClickListener {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Avatar")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            ImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri)
            startActivityForResult(intent, 100)
        }

        binding.productImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"

            startActivityForResult(intent, 101)
        }

        val spinner: Spinner = findViewById(R.id.categorySpinner)

        ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        binding.backToSArrow.setOnClickListener {
            val getID = intent.getStringExtra("getID")
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("getID", getID)
            startActivity(intent)
        }

        val prodCount: Int = Database.products.size
        val newID: Int = prodCount + 1
        val prodID = "PD" + String.format("%04d", newID)

        binding.addProdID.text = prodID

        binding.addProdBtn.setOnClickListener {
            val prodName = binding.addProdName.text.toString()
            val prodPrice = Integer.parseInt(binding.addProdPrice.text.toString())
            val prodStock = Integer.parseInt(binding.addProdStock.text.toString())
            val prodDesc = binding.addProdDesc.text.toString()
            val category = spinner.selectedItem.toString()
            val brand = binding.addBrand.text.toString()
            val fileName = "$prodID.jpg"
            val storageReference = FirebaseStorage.getInstance().getReference("Product/$fileName")

            if (!::ImageUri.isInitialized) {
                Toast.makeText(this, "Please Upload Image First.", Toast.LENGTH_SHORT).show()
                binding.productImageButton.requestFocus()
            } else if (prodName == "") {
                Toast.makeText(this, "Please Enter Product Name.", Toast.LENGTH_SHORT).show()
                binding.addProdName.requestFocus()
            } else if (prodPrice.toString() == "") {
                Toast.makeText(this, "Please Enter Product Price.", Toast.LENGTH_SHORT).show()
                binding.addProdPrice.requestFocus()
            } else if (prodStock.toString() == "") {
                Toast.makeText(this, "Please Enter Product Stock.", Toast.LENGTH_SHORT).show()
                binding.addProdStock.requestFocus()
            } else if (prodDesc == "") {
                Toast.makeText(this, "Please Enter Product Description.", Toast.LENGTH_SHORT).show()
                binding.addProdDesc.requestFocus()
            } else if (brand == "") {
                Toast.makeText(this, "Please Enter Product Brand.", Toast.LENGTH_SHORT).show()
                binding.addBrand.requestFocus()
            } else {
                Database.addProduct(
                    prodID,
                    prodName,
                    prodPrice,
                    prodStock,
                    prodDesc,
                    brand,
                    category
                )

                Log.e("Checking", ImageUri.toString())
                storageReference.putFile(ImageUri)
                    .addOnSuccessListener {
                        binding.productImageButton.setImageURI(null)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()

                    }.addOnCompleteListener{
                        Toast.makeText(this, "Added Successful!", Toast.LENGTH_LONG).show()
                        val getID = intent.getStringExtra("getID")
                        val intent = Intent(this, ProductListActivity::class.java)
                        intent.putExtra("getID", getID)
                        startActivity(intent)
                    }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            binding.productImageButton.setImageURI(ImageUri)

        } else if (requestCode == 101 && resultCode == AppCompatActivity.RESULT_OK) {
            ImageUri = data?.data!!
            binding.productImageButton.setImageURI(ImageUri)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.takeImageBtn.isEnabled = true
        }
    }
}