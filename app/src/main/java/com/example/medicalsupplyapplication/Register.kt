package com.example.medicalsupplyapplication

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.medicalsupplyapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var ImageUri: Uri
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.takeImageBtn.isEnabled = false

        binding.registerText.setTextColor(Color.BLUE)

        binding.showpass.setOnClickListener {
            if(binding.showpass.contentDescription.toString().equals("show")){
                binding.addCustPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showpass.contentDescription = "Hide"
            } else{
                binding.addCustPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showpass.contentDescription = "show"
            }
        }

        binding.showConPass.setOnClickListener {
            if(binding.showpass.contentDescription.toString().equals("show")){
                binding.addCustConfPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showpass.contentDescription = "Hide"
            } else{
                binding.addCustConfPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showpass.contentDescription = "show"
            }
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

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

        val custCount: Int = Database.customers.size
        val newID: Int = custCount+1
        val custID = "CT" + String.format("%04d",newID)

        binding.addCustBtn.setOnClickListener {
            val custName = binding.addCustName.text.toString()
            val custEmail = binding.addCustEmail.text.toString()
            val custPhone = binding.addCustPhone.text.toString()
            val custPass = binding.addCustPass.text.toString()
            val custConfPass = binding.addCustConfPass.text.toString()
            val address = binding.addAddress.text.toString()
            val fileName = "$custID.jpg"
            val storageReference = FirebaseStorage.getInstance().getReference("Customer/$fileName")

            if (this::ImageUri.isInitialized) {
                storageReference.putFile(ImageUri)
                    .addOnSuccessListener {
                        binding.profileImageButton.setImageURI(null)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Please Upload Image First.", Toast.LENGTH_SHORT).show()
                binding.profileImageButton.requestFocus()
            }

            if (custName == "") {
                Toast.makeText(this, "Please Enter Username.", Toast.LENGTH_SHORT).show()
                binding.addCustName.requestFocus()
            } else if (custEmail == "" || !Patterns.EMAIL_ADDRESS.matcher(custEmail).matches()) {
                Toast.makeText(this, "Please Enter Valid Email.", Toast.LENGTH_SHORT).show()
                binding.addCustEmail.requestFocus()
            } else if (custPhone == "") {
                Toast.makeText(this, "Please Enter Phone Number.", Toast.LENGTH_SHORT).show()
                binding.addCustPhone.requestFocus()
            } else if (custPass == "" || custPass.length < 8) {
                Toast.makeText(this, "Please Enter Password with at least 8 characters.", Toast.LENGTH_SHORT).show()
                binding.addCustPass.requestFocus()
            } else if (custConfPass == "") {
                Toast.makeText(this, "Please Enter Confirm Password.", Toast.LENGTH_SHORT).show()
                binding.addCustConfPass.requestFocus()
            } else if (custConfPass != custPass) {
                Toast.makeText(this, "Please enter confirm password same as password.", Toast.LENGTH_SHORT).show()
                binding.addCustConfPass.requestFocus()
            } else if (address == "") {
                Toast.makeText(this, "Please enter address.", Toast.LENGTH_SHORT).show()
                binding.addAddress.requestFocus()
            } else {
                auth.createUserWithEmailAndPassword(custEmail, custPass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = Firebase.auth.currentUser

                        user!!.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "Email sent.")
                                }
                            }

                        Database.addCustomer(custID, custName, custEmail, custPhone, custPass, custConfPass, address)
                        Toast.makeText(this, "Register Successful!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
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