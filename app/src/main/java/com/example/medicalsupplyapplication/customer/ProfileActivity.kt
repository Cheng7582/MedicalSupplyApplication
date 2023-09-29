package com.example.medicalsupplyapplication.customer


import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityProfileBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val custID = intent.getStringExtra("getID")

        Database.db.collection("Customer")
            .whereEqualTo("CustID", custID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val userName = document.getString("Username")
                    val email = document.getString("Email")
                    val phone = document.getString("Phone")
                    val address = document.getString("Address")

                    // Update the UI with the retrieved data
                    binding.showCustName.text = userName
                    binding.showCustEmail.text = email
                    binding.showCustPhone.text = phone
                    binding.showAddress.text = address

                    // Load the profile image from Firebase Storage
                    val storageRef = FirebaseStorage.getInstance().reference.child("Customer/$custID.jpg")
                    val localFile = File.createTempFile("tempImage", "jpeg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        binding.profileImage.setImageBitmap(bitmap)
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur during the database query
                // You may want to display an error message to the user
            }

        binding.upProfile.setTextColor(Color.BLUE)

        binding.updateCustBtn.setOnClickListener {
            val intent = Intent(this,UpdateProfileActivity::class.java)
            intent.putExtra("getID",custID)
            startActivity(intent)
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            intent.putExtra("getID",custID)
            startActivity(intent)
        }
    }
}