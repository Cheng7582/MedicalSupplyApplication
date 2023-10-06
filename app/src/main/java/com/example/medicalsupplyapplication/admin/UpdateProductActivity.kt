package com.example.medicalsupplyapplication.admin

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.database.model.Product
import com.example.medicalsupplyapplication.databinding.ActivityUpdateProductBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization
import com.example.medicalsupplyapplication.viewModel.ProductViewModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UpdateProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding
    private lateinit var ImageUri: Uri
    private val synchronization = Synchronization()
    private lateinit var productViewModel: ProductViewModel
    private var spinnerSelection: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getProdID = intent.getStringExtra("getProdID")
        val getID = intent.getStringExtra("getID")
        val getPosition = intent.getIntExtra("getIndex", 0)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        if (productViewModel.product.value == null) {
            if (synchronization.isNetworkAvailable(this)) {
                productViewModel.initOnlineProduct(getProdID ?: "")
            }
        }

        //Category Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.category,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        }

        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCategory = parentView.getItemAtPosition(position).toString()
                    spinnerSelection = position// Stored for later observer
                    productViewModel.product.value?.category = selectedCategory

                }

                override fun onNothingSelected(parentView: AdapterView<*>) {}
            }

        productViewModel.product.observe(this, Observer { newProduct ->

            binding.editProdID.text = newProduct.productID
            binding.editProdName.setText(newProduct.productName)
            binding.editProdPrice.setText(newProduct.price.toString())
            binding.editProdStock.setText(newProduct.stock.toString())
            binding.editBrand.setText(newProduct.brand)
            binding.editProdDesc.setText(newProduct.desc)
            binding.categorySpinner.setSelection(spinnerSelection)

        })

        binding.takeImageBtn.isEnabled = false
        if (synchronization.isNetworkAvailable(this)) {
            val storageRef = FirebaseStorage.getInstance().reference.child("Product/$getProdID.jpg")
            val localfile = File.createTempFile("tempImage", "jpeg")
            storageRef.getFile(localfile).addOnSuccessListener {

                //set image to view
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.productImageButton.setImageBitmap(bitmap)

                //set image uri for later upload
                ImageUri = Uri.fromFile(localfile)
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 111)

            } else {
                binding.takeImageBtn.isEnabled = true
            }

            binding.takeImageBtn.setOnClickListener {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Avatar")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
                ImageUri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri)
                startActivityForResult(intent, 100)
            }

            binding.productImageButton.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"

                startActivityForResult(intent, 101)
            }
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

                val product = Product(getProdID?:"",prodName,prodPrice,prodStock,prodDesc,brand, category, com.google.firebase.Timestamp.now())
                productViewModel.updateSingleProductOnline(product)

//                ------------------------Here is the problem----------------------
                if(synchronization.isNetworkAvailable(this)){
                    storageReference.putFile(ImageUri)
                        .addOnSuccessListener {
                            binding.productImageButton.setImageURI(null)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
                            Log.e("StorageError", "Image upload failed", exception)
                        }
                        .addOnCompleteListener {
                            Toast.makeText(this, "Updated Successful!", Toast.LENGTH_LONG).show()
                        }
                }

                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("getIndex", getPosition)
                intent.putExtra("getID", getID)
                intent.putExtra("getProdID", getProdID)
                startActivity(intent)
            }
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("getProdID", getProdID)
            intent.putExtra("getID", getID)
            startActivity(intent)
        }

        setTextChangedListener()
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

    private fun setTextChangedListener(){
        //Update viewModel when user edit
        binding.editProdName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productViewModel.product.value?.productName = s.toString()
            }
        })

        binding.editProdPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() != ""){
                    productViewModel.product.value?.price = Integer.parseInt(s.toString())
                }
            }
        })

        binding.editProdStock.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() != ""){
                    productViewModel.product.value?.stock = Integer.parseInt(s.toString())
                }
            }
        })

        binding.editBrand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productViewModel.product.value?.brand = s.toString()
            }
        })

        binding.editProdDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productViewModel.product.value?.desc = s.toString()
            }
        })
    }
}