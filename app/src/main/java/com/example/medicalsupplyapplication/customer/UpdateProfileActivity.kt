package com.example.medicalsupplyapplication.customer

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityUpdateProfileBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getID = intent.getStringExtra("getID")

        binding.upProfile.setTextColor(Color.BLUE)

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("getID",getID)
            startActivity(intent)
        }

        val storageRef = FirebaseStorage.getInstance().reference.child("Customer/$getID.jpg")
        val localfile = File.createTempFile("tempImage","jpeg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.profileImageButton.setImageBitmap(bitmap)
        }

        binding.takeImageBtn.isEnabled = false

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),111)
        }else{
            binding.takeImageBtn.isEnabled = true
        }

        binding.takeImageBtn.setOnClickListener{
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Avatar")
            values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera")
            ImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)!!
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,ImageUri)
            startActivityForResult(intent, 101)
        }

        binding.profileImageButton.setOnClickListener{
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent,100)
        }

        Database.db.collection("Customer").whereEqualTo("CustID",getID)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    binding.editCustName.setText(doc.get("Username").toString())
                    binding.editCustEmail.text = doc.get("Email").toString()
                    binding.editCustPhone.setText(doc.get("Phone").toString())
                    binding.editAddress.setText(doc.get("Address").toString())
                }
            }

        binding.editCustBtn.setOnClickListener {
            val custName = binding.editCustName.text.toString()
            val custPhone = binding.editCustPhone.text.toString()
            val custAddress = binding.editAddress.text.toString()
            val fileName = "$getID.jpg"
            val storageReference = FirebaseStorage.getInstance().getReference("Customer/$fileName")

            if(this::ImageUri.isInitialized){
                storageReference.putFile(ImageUri)
                    .addOnSuccessListener {
                        binding.profileImageButton.setImageURI(null)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this,"Failed to upload image", Toast.LENGTH_LONG).show()
                    }
                if(custName == "") {
                    Toast.makeText(this, "Please Enter Product Name.", Toast.LENGTH_SHORT).show()
                    binding.editCustName.requestFocus()
                }else if(binding.editCustPhone.text.toString() == "") {
                    Toast.makeText(this, "Please Enter Product Price.", Toast.LENGTH_SHORT).show()
                    binding.editCustPhone.requestFocus()
                }else if(binding.editAddress.text.toString() == "") {
                    Toast.makeText(this, "Please Enter Product Quantity.", Toast.LENGTH_SHORT).show()
                    binding.editAddress.requestFocus()
                }else{
                    Database.db.collection("Customer").document(getID.toString())
                        .update("Username", custName,
                            "Phone", custPhone,
                            "Address", custAddress)
                        .addOnSuccessListener {
                            Toast.makeText(this,"Updated Successful!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, ProfileActivity::class.java)
                            intent.putExtra("getID",getID)
                            startActivity(intent)
                        }
                }
            }else{
                Toast.makeText(this,"Image can't be empty.", Toast.LENGTH_LONG).show()
                binding.profileImageButton.requestFocus()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK){
            ImageUri = data?.data!!
            binding.profileImageButton.setImageURI(ImageUri)
        }else if(requestCode == 101 && resultCode == AppCompatActivity.RESULT_OK){
            binding.profileImageButton.setImageURI(ImageUri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            binding.takeImageBtn.isEnabled = true
        }
    }
}