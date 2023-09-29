package com.example.medicalsupplyapplication.admin

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.databinding.ActivityUpdateProductBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream

class UpdateProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding
    private lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getProdID = intent.getStringExtra("getProdID")
        val getID = intent.getStringExtra("getID")
        val getPosition = intent.getIntExtra("getIndex", 0)

        Database.db.collection("Product").whereEqualTo("ProductID", getProdID)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    binding.editProdID.text = doc.get("ProductID").toString()
                    binding.editProdName.setText(doc.get("ProductName").toString())
                    binding.editProdPrice.setText(doc.get("Price").toString())
                    binding.editProdStock.setText(doc.get("Stock").toString())
                    binding.editBrand.setText(doc.get("Brand").toString())
                    binding.editProdDesc.setText(doc.get("Desc").toString())
                }
            }

        ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("getProdID", getProdID)
            intent.putExtra("getID", getID)
            startActivity(intent)
        }

        val storageRef = FirebaseStorage.getInstance().reference.child("Product/$getProdID.jpg")
        val localfile = File.createTempFile("tempImage", "jpeg")
        storageRef.getFile(localfile).addOnSuccessListener {

            //set image to view
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.productImageButton.setImageBitmap(bitmap)

            //set image uri for later upload
            ImageUri = Uri.fromFile(localfile)
        }

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

        binding.editProdBtn.setOnClickListener {
            val prodName = binding.editProdName.text.toString()
            val prodPrice = Integer.parseInt(binding.editProdPrice.text.toString())
            val prodStock = Integer.parseInt(binding.editProdStock.text.toString())
            val prodDesc = binding.editProdDesc.text.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            val brand = binding.editBrand.text.toString()
            val fileName = "$getProdID.jpg"
            val storageReference = FirebaseStorage.getInstance().getReference("Product/$fileName")

            if (!::ImageUri.isInitialized) {
                Toast.makeText(this, "Please Upload Image First.", Toast.LENGTH_SHORT).show()
                binding.productImageButton.requestFocus()
            } else if (prodName == "") {
                Toast.makeText(this, "Please Enter Product Name.", Toast.LENGTH_SHORT).show()
                binding.editProdName.requestFocus()
            } else if (prodPrice.toString() == "") {
                Toast.makeText(this, "Please Enter Product Price.", Toast.LENGTH_SHORT).show()
                binding.editProdPrice.requestFocus()
            } else if (prodStock.toString() == "") {
                Toast.makeText(this, "Please Enter Product Quantity.", Toast.LENGTH_SHORT).show()
                binding.editProdStock.requestFocus()
            } else if (prodDesc == "") {
                Toast.makeText(this, "Please Enter Product Description.", Toast.LENGTH_SHORT).show()
                binding.editProdDesc.requestFocus()
            } else if (brand == "") {
                Toast.makeText(this, "Please Enter Product Brand.", Toast.LENGTH_SHORT).show()
                binding.editBrand.requestFocus()
            } else {

                Database.updateProduct(
                    getProdID.toString(),
                    prodName,
                    prodPrice,
                    prodStock,
                    prodDesc,
                    category,
                    brand,
                    getPosition
                )

                Log.e("Checking", ImageUri.toString())
//                ------------------------Here is the problem----------------------
                storageReference.putFile(ImageUri)
                    .addOnSuccessListener {
                        binding.productImageButton.setImageURI(null)
                    }
                    .addOnFailureListener {exception ->
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
                        Log.e("StorageError", "Image upload failed", exception)
                    }
                    .addOnCompleteListener {

                        Toast.makeText(this, "Updated Successful!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, ProductDetailsActivity::class.java)
                        intent.putExtra("getIndex", getPosition)
                        intent.putExtra("getID", getID)
                        intent.putExtra("getProdID", getProdID)
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